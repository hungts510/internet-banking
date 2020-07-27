package com.hungts.internetbanking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.TransactionMapper;
import com.hungts.internetbanking.model.entity.*;
import com.hungts.internetbanking.model.info.*;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.model.request.TransactionRequest;
import com.hungts.internetbanking.repository.*;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.PartnerService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EmailUtil;
import com.hungts.internetbanking.util.HttpGetWithEntity;
import com.hungts.internetbanking.util.PGPSecurity;
import com.hungts.internetbanking.util.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mapstruct.ap.shaded.freemarker.template.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sun.awt.geom.AreaOp;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserService userService;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    DebtorRepository debtorRepository;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    PartnerService partnerService;

    @Autowired
    TransactionSignatureRepository transactionSignatureRepository;

    @Override
    public AccountInfo createAccount(AccountInfo accountRequest) {
        Date currentDate = new Date();

        Account accountInsert = new Account();
        accountInsert.setAccountNumber(generateUniqueAccountNumber());
        accountInsert.setAccountType(accountRequest.getAccountType());
        accountInsert.setUserId(accountRequest.getUserId());
        accountInsert.setCreatedAt(currentDate);
        accountInsert.setUpdatedAt(currentDate);

        accountRepository.saveAccount(accountInsert);
        Account account = accountRepository.getAccountById(accountInsert.getId());
        return new AccountInfo(account);
    }

    private long generateUniqueAccountNumber() {
        long currentTimeMilliseconds = System.currentTimeMillis();
        String accountNumberSuffix = String.valueOf(currentTimeMilliseconds % 1000000);
        String accountNumberString = Constant.ACCOUNT_NUMBER_PREFIX + accountNumberSuffix;
        long accountNumber = Long.parseLong(accountNumberString);

        return accountNumber;
    }

    @Override
    public List<AccountInfo> getAllAccountByUserId(Integer userId) {
        List<Account> accountList = accountRepository.getAllAccountByUserId(userId);

        List<AccountInfo> accountInfoList = new ArrayList<>();
        for (Account account : accountList) {
            accountInfoList.add(new AccountInfo(account));
        }
        return accountInfoList;
    }

    @Override
    public AccountInfo getAccountInfoByAccountNumber(long accountNumber) {
        Account account = accountRepository.getCustomerAccountByAccountNumber(accountNumber);

        if (account == null) {
            throw new EzException("Account does not exist!");
        }

        UserInfo userInfo = userService.getUserById(account.getUserId());

        AccountInfo accountInfo = new AccountInfo(account);
        accountInfo.setAccountName(userInfo.getFullName());

        return accountInfo;
    }

    @Override
    public TransactionInfo receiverMoneyFromExternalBank(String fromBank, long amount, String description, long accountNumber, String signature) {
        Account account = accountRepository.getAccountFullInfoByAccountNumber(accountNumber);

        if (account == null) {
            throw new EzException("Account does not exist");
        }

        Transaction transaction = new Transaction();
        transaction.setFromBank(fromBank);
        transaction.setToBank(Constant.BANK_NAME);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setType(Constant.TransactionType.TRANSFER);
        transaction.setStatus(Constant.TransactionStatus.SUCCESS);
        transaction.setToAccountNumber(account.getAccountNumber());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());

        transactionRepository.saveTransaction(transaction);
        if (transaction.getId() == null) {
            throw new EzException("An error occurred while saving transaction");
        }

        long newBalance = account.getBalance() + transaction.getAmount();
        accountRepository.updateAccountBalance(newBalance, account.getId());

        TransactionSignature transactionSignature = new TransactionSignature();
        transactionSignature.setTransactionId(transaction.getId());
        transactionSignature.setSignature(signature);
        transactionSignature.setCreatedAt(new Date());
        transactionSignature.setUpdatedAt(new Date());
        transactionSignatureRepository.saveTransactionSignature(transactionSignature);
        return transactionMapper.transactionToTransactionInfo(transaction);
    }

    @Override
    public void payInToAccount(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account userAccount = accountRepository.getAccountFullInfoByAccountNumber(transactionRequest.getToAccountNumber());
        if (userAccount == null) {
            throw new EzException("Account does not exist");
        }

        Transaction transaction = new Transaction();
        transaction.setFromUserId(userInfo.getUserId());
        transaction.setToUserId(userAccount.getUserId());
        transaction.setFromBank(Constant.BANK_NAME);
        transaction.setToBank(Constant.BANK_NAME);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setType(Constant.TransactionType.PAY_IN);
        transaction.setStatus(Constant.TransactionStatus.SUCCESS);
        transaction.setToAccountNumber(userAccount.getAccountNumber());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());

        transactionRepository.saveTransaction(transaction);
        if (transaction.getId() == null) {
            throw new EzException("An error occurred while saving transaction");
        }

        long newBalance = userAccount.getBalance() + transaction.getAmount();
        accountRepository.updateAccountBalance(newBalance, userAccount.getId());
    }

    @Override
    public TransactionInfo createTransferMoneyTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account userSourceAccount = accountRepository.getAccountFullInfoByAccountNumber(transactionRequest.getFromAccountNumber());
        if (userSourceAccount == null || !userSourceAccount.getUserId().equals(userInfo.getUserId())) {
            throw new EzException("Source account does not exist");
        }

        if (userSourceAccount.getBalance() < transactionRequest.getAmount()) {
            throw new EzException("Balance not enough");
        }

        Transaction transaction = new Transaction();
        transaction.setFromUserId(userInfo.getUserId());
        transaction.setToUserId(transactionRequest.getToUserId());
        transaction.setFromBank(Constant.BANK_NAME);
        transaction.setToBank(transactionRequest.getToBank());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setType(Constant.TransactionType.TRANSFER);
        transaction.setStatus(Constant.TransactionStatus.PENDING);
        transaction.setFromAccountNumber(userSourceAccount.getAccountNumber());
        transaction.setToAccountNumber(transactionRequest.getToAccountNumber());
        transaction.setOtp(Utils.randomOTP());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        transaction.setPayFee(transactionRequest.isUserPayFee());

        transactionRepository.saveTransaction(transaction);
        if (transaction.getId() == null) {
            throw new EzException("An error occurred while saving transaction");
        }

        try {
            EmailUtil.sendTransferOTP(userInfo, transaction);
        } catch (Exception e) {
            throw new EzException("Fail to send email");
        }

        return transactionMapper.transactionToTransactionInfo(transaction);
    }

    @Override
    public void captureTransferTransaction(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Transaction transaction = transactionRepository.getUserTransactionByIdAndStatus(transactionRequest.getTransactionId(), userInfo.getUserId(), Constant.TransactionStatus.PENDING);
        if (transaction == null) {
            throw new EzException("Transaction not found");
        }

        if (System.currentTimeMillis() - transaction.getCreatedAt().getTime() > Constant.OTP_AVAILABLE_TIME) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("OTP is expired");
        }

        if (!transactionRequest.getOtp().equals(transaction.getOtp())) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("OTP invalid");
        }

        Account userSourceAccount = accountRepository.getAccountFullInfoByAccountNumber(transaction.getFromAccountNumber());
        if (userSourceAccount == null || !userSourceAccount.getUserId().equals(userInfo.getUserId())) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("Source account does not exist");
        }

        if (userSourceAccount.getBalance() < transaction.getAmount()) {
            transactionRepository.updateTransactionStatus(Constant.TransactionStatus.FAIL, transaction.getId());
            throw new EzException("Balance not enough");
        }

        Account destinationAccount = null;
        String transactionSignature = StringUtils.EMPTY;
        if (transaction.getFromBank().equals(transaction.getToBank())) {
            //Transfer internal
            destinationAccount = accountRepository.getAccountFullInfoByAccountNumber(transaction.getToAccountNumber());
            long newSourceBalance = userSourceAccount.getBalance() - transaction.getAmount();
            long newDestinationBalance = destinationAccount.getBalance() + transaction.getAmount();
            accountRepository.updateAccountBalance(newSourceBalance, userSourceAccount.getId());
            accountRepository.updateAccountBalance(newDestinationBalance, destinationAccount.getId());
        } else {
            transactionSignature = transferMoneyToExternalBank(transaction, userInfo.getFullName());
            long amountAndFee = transaction.getAmount();
            if (transaction.isPayFee()) {
                amountAndFee += Constant.TRANSFER_EXTERNAL_FEE;
            }
            long newSourceBalance = userSourceAccount.getBalance() - amountAndFee;
            accountRepository.updateAccountBalance(newSourceBalance, userSourceAccount.getId());
        }

        transactionRepository.updateTransactionStatus(Constant.TransactionStatus.SUCCESS, transaction.getId());

        if (ObjectUtils.isNotEmpty(transactionSignature)) {
            TransactionSignature transactionSignatureEntity = new TransactionSignature();
            transactionSignatureEntity.setTransactionId(transaction.getId());
            transactionSignatureEntity.setSignature(transactionSignature);
            transactionSignatureEntity.setCreatedAt(new Date());
            transactionSignatureEntity.setUpdatedAt(new Date());
            transactionSignatureRepository.saveTransactionSignature(transactionSignatureEntity);
        }

        if (transaction.getDebtId() != null && transaction.getDebtId() > 0) {
            debtorRepository.updateDebtorById(transaction.getDebtId(), transaction.getDescription(), new Date(), Constant.DebtStatus.PAID);
            Notification notification = new Notification();
            notification.setCreatedAt(new Date());
            notification.setUpdatedAt(new Date());
            notification.setDebtorId(transaction.getDebtId());
            notification.setStatus(Constant.NotificationStatus.NOT_READ);
            notification.setFromUserId(userInfo.getUserId());
            notification.setToUserId(transaction.getToUserId());
            notification.setContent("Tài khoản " + transaction.getFromAccountNumber() + " vừa thanh toán một nhắc nợ do bạn tạo!");
            notificationRepository.saveNotification(notification);
        }
    }

    @Override
    public TransactionMetaData getListAccountTransaction(AccountRequest accountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo.getRole().equals(Constant.UserRole.ROLE_CUSTOMER)) {
            Account account = accountRepository.getUserAccountByType(userInfo.getUserId(), Constant.AccountType.SPEND_ACCOUNT);
            if (!account.getAccountNumber().equals(accountRequest.getAccountNumber())) {
                throw new EzException("Account number invalid");
            }
        }

        TransactionMetaData transactionMetaData = new TransactionMetaData();
        Account userAccount = accountRepository.getCustomerAccountByAccountNumber(accountRequest.getAccountNumber());

        if (userAccount == null) {
            throw new EzException("Account does not exist");
        }

        if (accountRequest.getTransactionType().equals(Constant.TransactionType.TRANSFER)) {
            List<Transaction> transactions = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.TRANSFER, userAccount.getAccountNumber());
            transactionMetaData.setListTransfers(transactions.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        } else if (accountRequest.getTransactionType().equals(Constant.TransactionType.DEBT)) {
            List<Transaction> transactions = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.DEBT, userAccount.getAccountNumber());
            transactionMetaData.setListDebts(transactions.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        } else if (accountRequest.getTransactionType().equals(Constant.TransactionType.PAY_IN)) {
            List<Transaction> transactions = transactionRepository.getListTransactionByTypeToAccount(Constant.TransactionType.PAY_IN, userAccount.getAccountNumber());
            transactionMetaData.setListPayIn(transactions.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        } else if (accountRequest.getTransactionType().equals(Constant.TransactionType.ALL)) {
            List<Transaction> transactionsTransfers = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.TRANSFER, userAccount.getAccountNumber());
            transactionMetaData.setListTransfers(transactionsTransfers.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
            List<Transaction> transactionsDebts = transactionRepository.getListTransactionByTypeFromAccount(Constant.TransactionType.DEBT, userAccount.getAccountNumber());
            transactionMetaData.setListDebts(transactionsDebts.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
            List<Transaction> transactionsPayIn = transactionRepository.getListTransactionByTypeToAccount(Constant.TransactionType.PAY_IN, userAccount.getAccountNumber());
            transactionMetaData.setListPayIn(transactionsPayIn.stream().map(transaction -> transactionMapper.transactionToTransactionInfo(transaction)).collect(Collectors.toCollection(LinkedList::new)));
        }

        return transactionMetaData;
    }

    @Override
    public ResponseExternalAccountInfo getRSAAccountInfo(String bankName, Long accountNumber) {
        ResponseExternalAccountInfo accountInfo = new ResponseExternalAccountInfo();

        if (bankName.equals(Constant.PartnerName.BANK25)) {
            try {
                Map<String, Object> requestInfo = new HashMap<>();
                requestInfo.put("BankName", "30Bank");
                requestInfo.put("DestinationAccountNumber", accountNumber);
                requestInfo.put("iat", System.currentTimeMillis());

                ObjectMapper objectMapper = new ObjectMapper();
                String requestInfoString = objectMapper.writeValueAsString(requestInfo);
                Map<String, Object> mapRequest = new HashMap<>();

                String publicKeyContent = Constant.BANK25_RSA_PUBLIC_KEY.replaceAll("\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey publicKey = kf.generatePublic(keySpecX509);

                Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
//                Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                byte[] encryptData = cipher.doFinal(requestInfoString.getBytes());
                String encryptMessage = Base64.getEncoder().encodeToString(encryptData);
                mapRequest.put("Encrypted", encryptMessage);

                Signature privateSignature = Signature.getInstance("SHA256withRSA");

                String privateKeyContent = Constant.RSA_PRIVATE_KEY.replaceAll("\n", "").replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "");
                PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
                PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);
                privateSignature.initSign(privateKey);
                privateSignature.update(requestInfoString.getBytes(UTF_8));
                byte[] signature = privateSignature.sign();
                String signatureString = Base64.getEncoder().encodeToString(signature);
                mapRequest.put("Signed", signatureString);

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGetWithEntity httpGetWithEntity = new HttpGetWithEntity(Constant.PartnerAPI.BANK_25_ACCOUNT_INFO);
                StringEntity entity = new StringEntity(objectMapper.writeValueAsString(mapRequest));
                httpGetWithEntity.setEntity(entity);
                httpGetWithEntity.setHeader("Accept", "application/json");
                httpGetWithEntity.setHeader("Content-Type", "application/json");

                CloseableHttpResponse response = httpClient.execute(httpGetWithEntity);
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map<String, Object> mapResponse = objectMapper.readValue(body, Map.class);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new EzException("Get account info failed, response: " + mapResponse.get("message"));
                }

                accountInfo.setAccountName((String) mapResponse.get("TenKhachHang"));
                accountInfo.setAccountType((String) mapResponse.get("LoaiTaiKhoan"));
                accountInfo.setAccountNumber(accountNumber);
            } catch (Exception e) {
                throw new EzException("Can't get account info. Error: " + e.getMessage());
            }
        }
        return accountInfo;
    }

    @Override
    public ResponseExternalAccountInfo getPGPAccountInfo(String bankName, Long accountNumber) {
        ResponseExternalAccountInfo accountInfo = new ResponseExternalAccountInfo();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper objectMapper = new ObjectMapper();

        if (bankName.equals(Constant.PartnerName.BANK34)) {
            try {
                long currentTime = System.currentTimeMillis();
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

                String rawSignature = currentTime + Constant.BANK_SECRET_KEY;
//                String hashSignature = String.valueOf(messageDigest.digest(rawSignature.getBytes(UTF_8))); //implicit call
                String hashSignature = DigestUtils.sha256Hex(rawSignature.getBytes()); //implicit call

                HttpGet httpGet = new HttpGet(Constant.PartnerAPI.BANK_34_ACCOUNT_INFO + accountNumber);
                httpGet.setHeader("x-time", String.valueOf(currentTime));
                httpGet.setHeader("x-partner-code", Constant.BANK_NAME);
                httpGet.setHeader("x-signature", hashSignature);

                CloseableHttpResponse response = httpClient.execute(httpGet);
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map mapResponse = objectMapper.readValue(body, Map.class);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new EzException("Get account info failed, response: " + mapResponse.get("message"));
                }

                accountInfo.setAccountNumber(accountNumber);
                accountInfo.setAccountName((String) mapResponse.get("fullname"));
                accountInfo.setAccountType("Tài khoản thanh toán");
            } catch (Exception e) {
                throw new EzException("Can't get Bank34 account info. Error: " + e.getMessage());
            }
        }

        return accountInfo;
    }

    @Override
    public String transferMoneyToExternalBank(Transaction transaction, String accountName) {
        String signature = StringUtils.EMPTY;
        ObjectMapper objectMapper = new ObjectMapper();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        long transactionAmount = transaction.getAmount();
        if (!transaction.isPayFee()) {
            transactionAmount -= Constant.TRANSFER_EXTERNAL_FEE;
        }

        try {
            if (transaction.getToBank().equals(Constant.PartnerName.BANK25)) {
                //Bank25
                Map<String, Object> requestInfo = new HashMap<>();

                requestInfo.put("BankName", Constant.BANK_NAME);
                requestInfo.put("SourceAccountNumber", transaction.getFromAccountNumber());
                requestInfo.put("SourceAccountName", accountName);
                requestInfo.put("DestinationAccountNumber", transaction.getToAccountNumber());
                requestInfo.put("Amount", transactionAmount);
                requestInfo.put("Message", ObjectUtils.isEmpty(transaction.getDescription()) ? Constant.DEFAULT_TRANSFER_MESSAGE + transaction.getFromAccountNumber() : transaction.getDescription());
                requestInfo.put("iat", System.currentTimeMillis());

                String requestInfoString = objectMapper.writeValueAsString(requestInfo);
                Map<String, Object> mapRequest = new HashMap<>();

                String publicKeyContent = Constant.BANK25_RSA_PUBLIC_KEY.replaceAll("\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey publicKey = kf.generatePublic(keySpecX509);

                Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
//                Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                byte[] encryptData = cipher.doFinal(requestInfoString.getBytes());
                String encryptMessage = Base64.getEncoder().encodeToString(encryptData);
                mapRequest.put("Encrypted", encryptMessage);

                Signature privateSignature = Signature.getInstance("SHA256withRSA");

                String privateKeyContent = Constant.RSA_PRIVATE_KEY.replaceAll("\n", "").replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "");
                PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
                PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);
                privateSignature.initSign(privateKey);
                privateSignature.update(requestInfoString.getBytes(UTF_8));
                byte[] requestSignature = privateSignature.sign();
                String signatureString = Base64.getEncoder().encodeToString(requestSignature);
                mapRequest.put("Signed", signatureString);

                HttpPost httpPost = new HttpPost(Constant.PartnerAPI.BANK_25_RECHARGE_ACCOUNT);
                StringEntity entity = new StringEntity(objectMapper.writeValueAsString(mapRequest));
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");

                CloseableHttpResponse response = httpClient.execute(httpPost);
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map<String, Object> mapResponse = objectMapper.readValue(body, Map.class);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new EzException("Get account info failed, response: " + mapResponse.get("message"));
                }

                signature = signatureString;
            } else if (transaction.getToBank().equals(Constant.PartnerName.BANK34)) {
                //Bank34
                long currentTime = System.currentTimeMillis();
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("accNum", transaction.getToAccountNumber());
                requestBody.put("moneyAmount", transactionAmount);

                String requestBodyString = objectMapper.writeValueAsString(requestBody);
                String rawSignature = currentTime + requestBodyString + Constant.BANK_SECRET_KEY;
//                String hashSignature = String.valueOf(messageDigest.digest(rawSignature.getBytes(UTF_8))); //implicit call
                String hashSignature = DigestUtils.sha256Hex(rawSignature.getBytes()); //implicit call

                PGPSecurity pgpSecurity = new PGPSecurity();
                String encryptMessage = pgpSecurity.encryptAndSign(requestBodyString,
                        Constant.DEST_USER_EMAIL,
                        Constant.DEST_PASS_PHRASE,
                        PGPSecurity.ArmoredKeyPair.of(Constant.DEST_PRIVATE_KEYS, Constant.DEST_PUBLIC_KEYS),
                        Constant.BANK34_USER_EMAIL,
                        Constant.BANK34_PGP_PUBLIC_KEYS);

                requestBody.put("message", encryptMessage);

                HttpPost httpPost = new HttpPost(Constant.PartnerAPI.BANK_34_RECHARGE_ACCOUNT);
                httpPost.setHeader("x-time", String.valueOf(currentTime));
                httpPost.setHeader("x-partner-code", Constant.BANK_NAME);
                httpPost.setHeader("x-hash", hashSignature);
                StringEntity entity = new StringEntity(objectMapper.writeValueAsString(requestBody));
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");
                CloseableHttpResponse response = httpClient.execute(httpPost);
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map mapResponse = objectMapper.readValue(body, Map.class);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new EzException("Get account info failed, response: " + mapResponse.get("message"));
                }

                signature = encryptMessage;
            }
        } catch (Exception e) {
            throw new EzException("Can't transfer money, transaction " + e.getMessage());
        }
        return signature;
    }

    @Override
    public StatisticInfo bankStatistic(Date dateFrom, Date dateTo, String bankName) {
        List<Transaction> listTransactionToBank = new LinkedList<>();
        List<Transaction> listTransactionFromBank = new LinkedList<>();

        if (ObjectUtils.isNotEmpty(bankName)) {
            listTransactionToBank = transactionRepository.getListTransactionByStatusToBank(Constant.TransactionStatus.SUCCESS, bankName, dateFrom, dateTo);
            listTransactionFromBank = transactionRepository.getListTransactionByStatusFromBank(Constant.TransactionStatus.SUCCESS, bankName, dateFrom, dateTo);
        } else {
            listTransactionToBank = transactionRepository.getListTransactionByStatusToExternalBank(Constant.TransactionStatus.SUCCESS, Constant.BANK_NAME, dateFrom, dateTo);
            listTransactionFromBank = transactionRepository.getListTransactionByStatusFromExternalBank(Constant.TransactionStatus.SUCCESS, Constant.BANK_NAME, dateFrom, dateTo);
        }

        List<TransactionInfo> transactionInfosToBank = new LinkedList<>();
        List<TransactionInfo> transactionInfosFromBank = new LinkedList<>();

        long totalToBank = 0;
        long totalFromBank = 0;
        for (Transaction transaction : listTransactionToBank) {
            transactionInfosToBank.add(transactionMapper.transactionToTransactionInfo(transaction));
            totalToBank += transaction.getAmount();
        }

        for (Transaction transaction : listTransactionFromBank) {
            transactionInfosFromBank.add(transactionMapper.transactionToTransactionInfo(transaction));
            totalFromBank += transaction.getAmount();
        }

        StatisticInfo statisticInfo = new StatisticInfo();
        statisticInfo.setListTransactionTo(transactionInfosToBank);
        statisticInfo.setListTransactionFrom(transactionInfosFromBank);
        statisticInfo.setTotalTo(totalToBank);
        statisticInfo.setTotalFrom(totalFromBank);
        statisticInfo.setBankName(bankName);

        return statisticInfo;
    }
}

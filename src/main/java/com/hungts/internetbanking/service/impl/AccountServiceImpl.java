package com.hungts.internetbanking.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.TransactionMapper;
import com.hungts.internetbanking.model.entity.Account;
import com.hungts.internetbanking.model.entity.Notification;
import com.hungts.internetbanking.model.entity.Transaction;
import com.hungts.internetbanking.model.info.*;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.model.request.TransactionRequest;
import com.hungts.internetbanking.repository.AccountRepository;
import com.hungts.internetbanking.repository.DebtorRepository;
import com.hungts.internetbanking.repository.NotificationRepository;
import com.hungts.internetbanking.repository.TransactionRepository;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EmailUtil;
import com.hungts.internetbanking.util.PGPSecurity;
import com.hungts.internetbanking.util.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import zipkin2.Call;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
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
    public void payInToAccount(TransactionRequest transactionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account userAccount = accountRepository.getCustomerAccountByAccountNumber(transactionRequest.getToAccountNumber());
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
        if (transaction.getFromBank().equals(transaction.getToBank())) {
            //Transfer internal
            destinationAccount = accountRepository.getAccountFullInfoByAccountNumber(transaction.getToAccountNumber());
        } else {

        }

        long newSourceBalance = userSourceAccount.getBalance() - transaction.getAmount();
        long newDestinationBalance = destinationAccount.getBalance() + transaction.getAmount();

        accountRepository.updateAccountBalance(newSourceBalance, userSourceAccount.getId());
        accountRepository.updateAccountBalance(newDestinationBalance, destinationAccount.getId());
        transactionRepository.updateTransactionStatus(Constant.TransactionStatus.SUCCESS, transaction.getId());

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
        ResponseExternalAccountInfo accountInfo = null;

        if (bankName.equals(Constant.PartnerName.BANK25)) {
            try {
                Map<String, Object> requestInfo = new HashMap<>();
//                requestInfo.put("BankName", "30Bank");
//                requestInfo.put("DestinationAccountNumber", accountNumber);
//                requestInfo.put("iat", System.currentTimeMillis());

                requestInfo.put("BankName", "30Bank");
                requestInfo.put("SourceAccountNumber", "102102102");
                requestInfo.put("SourceAccountName", "Nguyễn Thanh Nam");
                requestInfo.put("DestinationAccountNumber", accountNumber);
                requestInfo.put("Amount", 50000);
                requestInfo.put("Message", "30Bank test recharge API");
                requestInfo.put("iat", System.currentTimeMillis());

                ObjectMapper objectMapper = new ObjectMapper();
                String requestInfoString = objectMapper.writeValueAsString(requestInfo);
                Map<String, Object> mapRequest = new HashMap<>();

                String publicKeyContent = Constant.BANK25_RSA_PUBLIC_KEY.replaceAll("\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
                KeyFactory kf = KeyFactory.getInstance("RSA");
                PublicKey publicKey =  kf.generatePublic(keySpecX509);

                Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
//                Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                byte[] encryptData = cipher.doFinal(requestInfoString.getBytes());
                String encryptMessage = Base64.getEncoder().encodeToString(encryptData);
                mapRequest.put("Encrypted", encryptMessage);

                Signature privateSignature = Signature.getInstance("SHA256withRSA");

                String privateKeyContent = Constant.RSA_PRIVATE_KEY.replaceAll("\n", "").replace("-----BEGIN RSA PRIVATE KEY-----", "").replace("-----END RSA PRIVATE KEY-----", "");
                PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
                PrivateKey privateKey = kf.generatePrivate(keySpecPKCS8);;
                privateSignature.initSign(privateKey);
                privateSignature.update(requestInfoString.getBytes(UTF_8));
                byte[] signature = privateSignature.sign();
                String signatureString = Base64.getEncoder().encodeToString(signature);
                mapRequest.put("Signed", signatureString);

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(Constant.PartnerAPI.BANK_25_ACCOUNT_INFO);
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

                accountInfo = objectMapper.convertValue(mapResponse.get("data"), ResponseExternalAccountInfo.class);

            } catch (Exception e) {
                throw new EzException("Can't get account info. Error: " + e.getMessage());
            }
        }
        return accountInfo;
    }

    @Override
    public ResponseExternalAccountInfo getPGPAccountInfo(String bankName, Long accountNumber) {
        ResponseExternalAccountInfo accountInfo = null;

        if (bankName.equals(Constant.PartnerName.PGP30BANK)) {
            try {
                Map<String, Object> requestInfo = new HashMap<>();
                requestInfo.put("account_number", accountNumber);
                requestInfo.put("request_time", System.currentTimeMillis());

                ObjectMapper objectMapper = new ObjectMapper();
                String requestInfoString = objectMapper.writeValueAsString(requestInfo);

                PGPSecurity pgpSecurity = new PGPSecurity();
                String encryptMessage = null;

                try {
                    encryptMessage = pgpSecurity.encryptAndSign(requestInfoString,
                            Constant.SOURCE_USER_EMAIL,
                            Constant.SOURCE_PASS_PHRASE,
                            PGPSecurity.ArmoredKeyPair.of(Constant.SOURCE_PRIVATE_KEYS, Constant.SOURCE_PUBLIC_KEYS),
                            Constant.DEST_USER_EMAIL,
                            Constant.DEST_PUBLIC_KEYS);

                    System.out.printf(encryptMessage);
                } catch (Exception e) {
                    throw new EzException("Message invalid: " + e.getMessage());
                }


                Map<String, Object> mapRequest = new HashMap<>();
                mapRequest.put("message", encryptMessage);

                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(Constant.PartnerAPI.BANK_30_SUB_ACCOUNT_INFO);
                StringEntity entity = new StringEntity(objectMapper.writeValueAsString(mapRequest));
                httpPost.setEntity(entity);
                httpPost.setHeader("partner-code", Constant.BANK_NAME);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-Type", "application/json");

                CloseableHttpResponse response = httpClient.execute(httpPost);
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                Map<String, Object> mapResponse = objectMapper.readValue(body, Map.class);
                if ((int) mapResponse.get("code") != 0) {
                    throw new EzException("Get account info failed, response: " + mapResponse.get("message"));
                }

                accountInfo = objectMapper.convertValue(mapResponse.get("data"), ResponseExternalAccountInfo.class);
                System.out.println(response);
            } catch (Exception e) {
                throw new EzException("Can't get account info. Error: " + e.getMessage());
            }
        }
        return accountInfo;
    }
}

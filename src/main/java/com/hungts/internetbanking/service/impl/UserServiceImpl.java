package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.DebtorMapper;
import com.hungts.internetbanking.mapper.NotificationMapper;
import com.hungts.internetbanking.mapper.TransactionMapper;
import com.hungts.internetbanking.mapper.UserMapper;
import com.hungts.internetbanking.model.entity.*;
import com.hungts.internetbanking.model.info.*;
import com.hungts.internetbanking.model.request.DebtorRequest;
import com.hungts.internetbanking.model.request.RefreshTokenRequest;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.model.response.AuthenticateResponse;
import com.hungts.internetbanking.repository.*;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EmailUtil;
import com.hungts.internetbanking.util.EncryptPasswordUtils;
import com.hungts.internetbanking.util.JwtTokenUtil;
import com.hungts.internetbanking.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AccountService accountService;

    @Autowired
    ReceiverRepository receiverRepository;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;

    @Autowired
    DebtorRepository debtorRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    DebtorMapper debtorMapper;

    @Autowired
    TransactionMapper transactionMapper;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    public UserInfo createUser(UserRequest userRequest) throws EzException {
        if (Utils.isEmpty(userRequest.getFullname()) || Utils.isEmpty(userRequest.getEmail())
                || Utils.isEmpty(userRequest.getPhone()) || Utils.isEmpty(userRequest.getPassword())) {
            throw new EzException("Missing field");
        }

        if (findUserByEmail(userRequest.getEmail()) != null) {
            throw new EzException("User email is exists!");
        }

        if (findUserByPhoneNumber(userRequest.getPhone()) != null) {
            throw new EzException("User phone number is exists");
        }

        String encryptPassword = EncryptPasswordUtils.encryptPassword(userRequest.getPassword());
        User insertUser = new User();
        insertUser.setEmail(userRequest.getEmail());
        insertUser.setPhone(userRequest.getPhone());
        insertUser.setFullName(userRequest.getFullname());
        insertUser.setPassword(encryptPassword);
        userRepository.insertUser(insertUser);

        AccountInfo accountRequest = new AccountInfo();
        accountRequest.setUserId((insertUser.getId()));
        accountRequest.setAccountType(Constant.AccountType.SPEND_ACCOUNT);
        AccountInfo accountInfo = accountService.createAccount(accountRequest);

        roleRepository.saveUserRole(insertUser.getId(), Constant.UserRole.ROLE_CUSTOMER);

        return userMapper.userToUserInfo(insertUser);
    }

    @Override
    public UserInfo findUserByEmail(String email) throws EzException {
        if (Utils.isBlank(email)) {
            throw new EzException("Missing email");
        }

        User currentUser = userRepository.getUserByEmail(email);
        UserInfo userInfo = userMapper.userToUserInfo(currentUser);
        return userInfo;
    }

    @Override
    public UserInfo findUserByPhoneNumber(String phoneNumber) throws EzException {
        if (Utils.isBlank(phoneNumber)) {
            throw new EzException("Missing phone number");
        }

        User currentUser = userRepository.getUserByPhoneNumber(phoneNumber);
        UserInfo userInfo = userMapper.userToUserInfo(currentUser);

        if (userInfo == null) {
            return null;
        }

        Role role = roleRepository.getRoleFromUserId(userInfo.getUserId());

        if (role == null) {
            return null;
        }
        userInfo.setRole(role.getId());
        return userInfo;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);
        Role role = roleRepository.getRoleFromUserId(userInfo.getUserId());
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));

        if (userInfo == null) {
            throw new UsernameNotFoundException("User not found with username: " + phoneNumber);
        }
        return new org.springframework.security.core.userdetails.User(userInfo.getPhone(), userInfo.getPassword(), authorities);
    }

    @Override
    public void changePassword(String phoneNumber, String oldPassword, String newPassword) {
        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);

        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        boolean isCorrectPassword = EncryptPasswordUtils.checkPassword(oldPassword, userInfo.getPassword());
        if (!isCorrectPassword) {
            throw new EzException("Old password not matched");
        }

        String userPassword = EncryptPasswordUtils.encryptPassword(newPassword);
        userRepository.updatePassword(userInfo.getUserId(), userPassword);
    }

    @Override
    public void sendEmailResetPassword(String email) {
        UserInfo userInfo = findUserByEmail(email);

        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        String otp = Utils.randomOTP();
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setUserId(userInfo.getUserId());
        resetPasswordRequest.setOtp(otp);
        resetPasswordRequest.setCreatedAt(new Date());
        resetPasswordRequest.setStatus(0);

        resetPasswordRepository.saveResetPasswordRequest(resetPasswordRequest);
        EmailUtil.sendResetPasswordEmail(userInfo, otp);
    }

    @Override
    public String resetPassword(String email, String otp) {
        UserInfo userInfo = findUserByEmail(email);

        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        ResetPasswordRequest resetPasswordRequest = resetPasswordRepository.getLastResetPasswordRequestByUserId(userInfo.getUserId());
        if (!resetPasswordRequest.getOtp().equals(otp)) {
            throw new EzException("Incorrect OTP");
        }

        if ((System.currentTimeMillis() - resetPasswordRequest.getCreatedAt().getTime()) > Constant.OTP_AVAILABLE_TIME) {
            throw new EzException("OTP code is expired");
        }

        String encryptDefaultPassword = EncryptPasswordUtils.encryptPassword(Constant.DEFAULT_PASSWORD);
        userRepository.updatePassword(userInfo.getUserId(), encryptDefaultPassword);
        final UserDetails userDetails = loadUserByUsername(userInfo.getPhone());
        final String token = JwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public DebtorInfo saveDebtor(DebtorRequest debtorRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Debtor insertDebtor = new Debtor();
        insertDebtor.setUserId(userInfo.getUserId());
        insertDebtor.setDebtorAccountNumber(debtorRequest.getDebtorAccountNumber());
        insertDebtor.setAmount(debtorRequest.getAmount());
        insertDebtor.setDescription(debtorRequest.getDescription());
        insertDebtor.setCreatedAt(new Date());
        insertDebtor.setUpdatedAt(new Date());
        insertDebtor.setStatus(1);

        debtorRepository.saveDebtor(insertDebtor);
        Debtor debtor = debtorRepository.getDebtorById(insertDebtor.getId());
        return debtorMapper.debtorToDebtorInfo(debtor);
    }

    @Override
    public List<DebtorInfo> getListDebtors(DebtorRequest debtorRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account account = accountRepository.getUserAccountByType(userInfo.getUserId(), 1);

        List<Debtor> debtorList = debtorRepository.getListDebtorsByUserId(userInfo.getUserId());
        List<DebtorInfo> debtorInfoList = new LinkedList<>();
        for (Debtor debtor : debtorList) {
            DebtorInfo debtorInfo = debtorMapper.debtorToDebtorInfo(debtor);
            debtorInfo.setReceiverName(userInfo.getFullName());
            debtorInfo.setReceiverAccountNumber(account.getAccountNumber());
            debtorInfoList.add(debtorInfo);
        }
        return debtorInfoList;
    }

    @Override
    public List<DebtorInfo> getListDebts(DebtorRequest debtorRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account spendAccount = accountRepository.getUserAccountByType(userInfo.getUserId(), Constant.AccountType.SPEND_ACCOUNT);
        List<Debtor> debtorList = debtorRepository.getListDebtsByAccountNumber(spendAccount.getAccountNumber());
        List<DebtorInfo> debtorInfoList = new LinkedList<>();
        for (Debtor debtor : debtorList) {
            DebtorInfo debtorInfo = debtorMapper.debtorToDebtorInfo(debtor);
            Account account = accountRepository.getUserAccountByType(debtorInfo.getUserId(), Constant.AccountType.SPEND_ACCOUNT);
            debtorInfo.setReceiverAccountNumber(account.getAccountNumber());
            debtorInfoList.add(debtorInfo);
        }
        return debtorInfoList;
    }

    @Override
    public void cancelDebt(DebtorRequest debtorRequest) {
        Debtor debtor = debtorRepository.getDebtorById(debtorRequest.getDebtId());
        if (debtor == null) {
            throw new EzException("Debtor does not exist");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);

        Account account = accountRepository.getUserAccountByType(userInfo.getUserId(), Constant.AccountType.SPEND_ACCOUNT);

        if (account == null) {
            throw new EzException("Debtor account does not exist");
        }

        if (!debtor.getUserId().equals(userInfo.getUserId()) && !account.getAccountNumber().equals(debtor.getDebtorAccountNumber())) {
            throw new EzException("Debtor not belong to this user");
        }

        debtorRepository.updateDebtorById(debtor.getId(), debtorRequest.getDescription(), new Date(), Constant.DebtStatus.CANCEL);
        Notification notification = new Notification();
        notification.setCreatedAt(new Date());
        notification.setUpdatedAt(new Date());
        notification.setDebtorId(debtor.getId());
        notification.setStatus(Constant.NotificationStatus.NOT_READ);

        if (debtor.getUserId().equals(userInfo.getUserId())) {
            Account debtorAccount = accountRepository.getCustomerAccountByAccountNumber(debtor.getDebtorAccountNumber());
            notification.setFromUserId(userInfo.getUserId());
            notification.setToUserId(debtorAccount.getUserId());
            notification.setContent("Bạn có một nhắc nợ từ tài khoản: " + account.getAccountNumber() + " vừa được hủy!");
        } else if (account.getAccountNumber().equals(debtor.getDebtorAccountNumber())) {
            notification.setFromUserId(account.getUserId());
            notification.setToUserId(debtor.getUserId());
            notification.setContent("Tài khoản " + account.getAccountNumber() + " vừa hủy một nhắc nợ do bạn tạo!");
        }

        notificationRepository.saveNotification(notification);
    }

    @Override
    public TransactionInfo payDebt(DebtorRequest debtorRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Debtor debtor = debtorRepository.getDebtorById(debtorRequest.getDebtId());
        if (debtor == null) {
            throw new EzException("Debtor does not exist");
        }

        Long debtorAccountNumber = debtor.getDebtorAccountNumber();
        Account debtorAccount = accountRepository.getAccountFullInfoByAccountNumber(debtorAccountNumber);

        if (debtorAccount.getBalance() < debtor.getAmount()) {
            throw new EzException("Not enough balance");
        }

        Account destinationAccount = accountRepository.getUserAccountByType(debtor.getUserId(), Constant.AccountType.SPEND_ACCOUNT);
        if (destinationAccount == null) {
            throw new EzException("Destination account does not exist");
        }

        Transaction transaction = new Transaction();
        transaction.setFromUserId(debtorAccount.getUserId());
        transaction.setToUserId(debtor.getUserId());
        transaction.setFromBank(Constant.BANK_NAME);
        transaction.setToBank(Constant.BANK_NAME);
        transaction.setAmount(debtor.getAmount());
        transaction.setDescription(debtorRequest.getDescription());
        transaction.setType(Constant.TransactionType.DEBT);
        transaction.setStatus(Constant.TransactionStatus.PENDING);
        transaction.setFromAccountNumber(debtorAccountNumber);
        transaction.setToAccountNumber(destinationAccount.getAccountNumber());
        transaction.setOtp(Utils.randomOTP());
        transaction.setDebtId(debtor.getId());
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());

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
    public DebtorInfo getDebtInfoById(Integer debtId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User does not exist");
        }

        Account account = accountRepository.getUserAccountByType(userInfo.getUserId(), Constant.AccountType.SPEND_ACCOUNT);
        Debtor debtor = debtorRepository.getDebtorById(debtId);

        if (!debtor.getUserId().equals(userInfo.getUserId()) && !debtor.getDebtorAccountNumber().equals(account.getAccountNumber())) {
            throw new EzException("Debt not belong to user");
        }

        return debtorMapper.debtorToDebtorInfo(debtor);
    }

    @Override
    public UserInfo getUserById(Integer userId) {
        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new EzException("User does not exist");
        }

        return userMapper.userToUserInfo(user);
    }

    @Override
    public List<NotificationInfo> getListUserNotification(Integer userId) {
        List<Notification> notificationList = notificationRepository.getListUserNotification(userId);

        return notificationList.stream().map(notification -> notificationMapper.notificationToNotificationInfo(notification)).collect(Collectors.toList());
    }

    @Override
    public NotificationInfo updateNotificationStatus(Integer notificationId) {
        Notification notification = notificationRepository.getNotificationById(notificationId);
        if (notification == null) {
            throw new EzException("Notificaiton does not exist");
        }

        notificationRepository.updateNotificationStatus(Constant.NotificationStatus.READ, notification.getId());
        NotificationInfo notificationInfo =  notificationMapper.notificationToNotificationInfo(notification);
        notificationInfo.setStatus(Constant.NotificationStatus.READ);
        return notificationInfo;
    }

    @Override
    public void readAllNotificationByUserId(Integer userId) {
        notificationRepository.updateAllUserNotificationStatus(Constant.NotificationStatus.READ, userId);
    }

    @Override
    public AuthenticateResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        UserDetails userDetails = loadUserByUsername(refreshTokenRequest.getPhone());

        User user = userRepository.getUserByPhoneNumber(userDetails.getUsername());
        if (!refreshTokenRequest.getRefreshToken().equals(user.getRefreshToken())) {
            throw new EzException("Refresh token not match");
        }

        AuthenticateResponse authenticateResponse = new AuthenticateResponse();
        authenticateResponse.setAccessToken(jwtTokenUtil.generateToken(userDetails));
        return authenticateResponse;
    }

    public void saveRefreshToken(String refreshToken, String phone) {
        userRepository.saveRefreshToken(refreshToken, phone);
    }
}

package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.DebtorMapper;
import com.hungts.internetbanking.mapper.UserMapper;
import com.hungts.internetbanking.model.entity.*;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.DebtorInfo;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.DebtorRequest;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.repository.*;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EmailUtil;
import com.hungts.internetbanking.util.EncryptPasswordUtils;
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
    DebtorMapper debtorMapper;

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
    public void resetPassword(String email, String otp) {
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

        List<Debtor> debtorList = debtorRepository.getListDebtorsByUserId(userInfo.getUserId());
        List<DebtorInfo> debtorInfoList = debtorList.stream().map(debtor -> debtorMapper.debtorToDebtorInfo(debtor)).collect(Collectors.toCollection(LinkedList::new));
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
        List<DebtorInfo> debtorInfoList = debtorList.stream().map(debtor -> debtorMapper.debtorToDebtorInfo(debtor)).collect(Collectors.toCollection(LinkedList::new));
        return debtorInfoList;
    }

    @Override
    public void cancelDebt(DebtorRequest debtorRequest) {
        Debtor debtor = debtorRepository.getDebtorById(debtorRequest.getDebtId());
        if (debtor == null) {
            throw new EzException("Debtor does not exist");
        }

        debtorRepository.updateDebtorById(debtor.getId(), debtorRequest.getDescription(), new Date(), Constant.DebtStatus.CANCEL);
    }
}

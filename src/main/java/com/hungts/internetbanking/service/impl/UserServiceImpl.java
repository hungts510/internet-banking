package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.UserMapper;
import com.hungts.internetbanking.model.entity.ResetPasswordRequest;
import com.hungts.internetbanking.model.entity.Role;
import com.hungts.internetbanking.model.entity.User;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.repository.ReceiverRepository;
import com.hungts.internetbanking.repository.ResetPasswordRepository;
import com.hungts.internetbanking.repository.RoleRepository;
import com.hungts.internetbanking.repository.UserRepository;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EmailUtil;
import com.hungts.internetbanking.util.EncryptPasswordUtils;
import com.hungts.internetbanking.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    AccountService accountService;

    @Autowired
    ReceiverRepository receiverRepository;

    @Autowired
    ResetPasswordRepository resetPasswordRepository;

    @Override
    public void createUser(UserRequest userRequest) throws EzException {
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
        Role role = roleRepository.getRoleFromUserId(userInfo.getUserId());
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
}

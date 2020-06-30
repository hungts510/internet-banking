package com.hungts.internetbanking.controller;

import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.DebtorInfo;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.*;
import com.hungts.internetbanking.model.request.ChangePasswordRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.ReceiverService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = ContextPath.User.USER)
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ReceiverService receiverService;

    @Autowired
    AccountService accountService;

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @RequestMapping(value = ContextPath.User.CREATE, method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        UserInfo userInfo = userService.createUser(userRequest);

        ResponseBody responseBody = new ResponseBody(0, "Success", userInfo);
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN') ")
    @RequestMapping(value = ContextPath.User.INFO, method = RequestMethod.GET)
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);

        ResponseBody responseBody;
        if (userInfo != null) {
            responseBody = new ResponseBody(0, "Success", userInfo);
        } else {
            responseBody = new ResponseBody(1, "Fail", "User does not exist!");
        }

        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.User.SAVE_RECEIVER, method = RequestMethod.POST)
    public ResponseEntity<?> saveReceiver(@RequestBody AccountRequest accountRequest) {
        if (accountRequest.getAccountNumber() == null || accountRequest.getAccountNumber() <= 0) {
            throw new EzException("Missing field account number");
        }

        if (StringUtils.isBlank(accountRequest.getAccountName())) {
            throw new EzException("Missing field account name");
        }

        receiverService.createReceiver(accountRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.User.LIST_ACCOUNT, method = RequestMethod.GET)
    public ResponseEntity<?> listAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User not exist");
        }

        List<AccountInfo> accountInfoList = accountService.getAllAccountByUserId(userInfo.getUserId());
        ResponseBody responseBody = new ResponseBody(0, "Success", accountInfoList);
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.User.UPDATE_RECEIVER, method = RequestMethod.POST)
    public ResponseEntity<?> updateReceiver(@RequestBody AccountRequest accountRequest) {
        if (accountRequest.getAccountNumber() == null || accountRequest.getAccountNumber() <= 0) {
            throw new EzException("Missing field account number");
        }

        if (StringUtils.isBlank(accountRequest.getAccountName())) {
            throw new EzException("Missing field account name");
        }

        receiverService.updateReceiver(accountRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.User.REMOVE_RECEIVER, method = RequestMethod.POST)
    public ResponseEntity<?> removeReceiver(@RequestBody AccountRequest accountRequest) {
        if (accountRequest.getAccountNumber() == null || accountRequest.getAccountNumber() <= 0) {
            throw new EzException("Missing field account number");
        }

        receiverService.removeReceiver(accountRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.User.CHANGE_PASSWORD, method = RequestMethod.POST)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (StringUtils.isBlank(changePasswordRequest.getOldPassword()) || StringUtils.isBlank(changePasswordRequest.getNewPassword())) {
            throw new EzException("Missing old password or new password");
        }

        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new EzException("Old password and new password are the same");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        userService.changePassword(phoneNumber, changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @RequestMapping(value = ContextPath.User.FORGOT_PASSWORD, method = RequestMethod.POST)
    public ResponseEntity<?> forgetPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (StringUtils.isBlank(changePasswordRequest.getEmail())) {
            throw new EzException("Missing email");
        }

        userService.sendEmailResetPassword(changePasswordRequest.getEmail());
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @RequestMapping(value = ContextPath.User.RESET_PASSWORD, method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        if (StringUtils.isBlank(changePasswordRequest.getEmail())) {
            throw new EzException("Missing email");
        }

        if (StringUtils.isBlank(changePasswordRequest.getOtp())) {
            throw new EzException("Missing otp");
        }

        userService.resetPassword(changePasswordRequest.getEmail(), changePasswordRequest.getOtp());
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @RequestMapping(value = ContextPath.User.SAVE_DEBTOR, method = RequestMethod.POST)
    public ResponseEntity<?> saveDebtor(@RequestBody DebtorRequest debtorRequest) {
        if (debtorRequest.getDebtorAccountNumber() == null || debtorRequest.getDebtorAccountNumber() <= 0) {
            throw new EzException("Missing account number field");
        }

        if (debtorRequest.getAmount() == null || debtorRequest.getAmount() <= 0) {
            throw new EzException("Missing amount field");
        }

        DebtorInfo debtorInfo = userService.saveDebtor(debtorRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success", debtorInfo);
        return EzResponse.response(responseBody);
    }
}
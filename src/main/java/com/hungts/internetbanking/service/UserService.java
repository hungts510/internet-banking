package com.hungts.internetbanking.service;

import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.DebtorInfo;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.DebtorRequest;
import com.hungts.internetbanking.model.request.UserRequest;

public interface UserService {
    UserInfo createUser(UserRequest userRequest) throws EzException;
    
    UserInfo findUserByEmail(String email) throws EzException;

    UserInfo findUserByPhoneNumber(String phoneNumber) throws EzException;

    void changePassword(String phoneNumber, String oldPassword, String newPassword);

    void sendEmailResetPassword(String email);

    void resetPassword(String email, String otp);

    DebtorInfo saveDebtor(DebtorRequest debtorRequest);
}

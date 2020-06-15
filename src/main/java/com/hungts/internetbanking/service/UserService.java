package com.hungts.internetbanking.service;

import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.UserRequest;

public interface UserService {
    void createUser(UserRequest userRequest) throws EzException;
    
    UserInfo findUserByEmail(UserRequest userRequest) throws EzException;
}

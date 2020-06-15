package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.UserMapper;
import com.hungts.internetbanking.model.entity.User;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.repository.UserRepository;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EncryptPasswordUtils;
import com.hungts.internetbanking.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserMapper userMapper;

    @Override
    public void createUser(UserRequest userRequest) throws EzException {
        if (Utils.isEmpty(userRequest.getFullname()) || Utils.isEmpty(userRequest.getEmail())
                || Utils.isEmpty(userRequest.getPhone()) || Utils.isEmpty(userRequest.getPassword())) {
            throw new EzException("Missing field");
        }

        if (findUserByEmail(userRequest) != null) {
            throw new EzException("User email is exists!");
        }

        if (findUserByPhoneNumber(userRequest)!= null) {
            throw new EzException("User phone number is exists");
        }

        String encryptPassword = EncryptPasswordUtils.encryptPassword(userRequest.getPassword());
        userRepository.insertUser(userRequest.getFullname(), userRequest.getEmail(), userRequest.getPhone(), encryptPassword);
    }

    @Override
    public UserInfo findUserByEmail(UserRequest userRequest) throws EzException {
        if (Utils.isBlank(userRequest.getEmail())) {
            throw new EzException("Missing email");
        }

        User currentUser = userRepository.getUserByEmail(userRequest.getEmail());
        UserInfo userInfo =  userMapper.userToUserInfo(currentUser);
        return userInfo;
    }

    @Override
    public UserInfo findUserByPhoneNumber(UserRequest userRequest) throws EzException {
        if (Utils.isBlank(userRequest.getPhone())) {
            throw new EzException("Missing phone number");
        }

        User currentUser = userRepository.getUserByPhoneNumber(userRequest.getPhone());
        UserInfo userInfo =  userMapper.userToUserInfo(currentUser);
        return userInfo;
    }
}

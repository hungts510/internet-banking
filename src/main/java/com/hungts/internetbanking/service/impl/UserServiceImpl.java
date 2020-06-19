package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.UserMapper;
import com.hungts.internetbanking.model.entity.Role;
import com.hungts.internetbanking.model.entity.User;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.repository.RoleRepository;
import com.hungts.internetbanking.repository.UserRepository;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EncryptPasswordUtils;
import com.hungts.internetbanking.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

    @Override
    public void createUser(UserRequest userRequest) throws EzException {
        if (Utils.isEmpty(userRequest.getFullname()) || Utils.isEmpty(userRequest.getEmail())
                || Utils.isEmpty(userRequest.getPhone()) || Utils.isEmpty(userRequest.getPassword())) {
            throw new EzException("Missing field");
        }

        if (findUserByEmail(userRequest.getEmail()) != null) {
            throw new EzException("User email is exists!");
        }

        if (findUserByPhoneNumber(userRequest.getPhone())!= null) {
            throw new EzException("User phone number is exists");
        }

        String encryptPassword = EncryptPasswordUtils.encryptPassword(userRequest.getPassword());
        userRepository.insertUser(userRequest.getFullname(), userRequest.getEmail(), userRequest.getPhone(), encryptPassword);
    }

    @Override
    public UserInfo findUserByEmail(String email) throws EzException {
        if (Utils.isBlank(email)) {
            throw new EzException("Missing email");
        }

        User currentUser = userRepository.getUserByEmail(email);
        UserInfo userInfo =  userMapper.userToUserInfo(currentUser);
        return userInfo;
    }

    @Override
    public UserInfo findUserByPhoneNumber(String phoneNumber) throws EzException {
        if (Utils.isBlank(phoneNumber)) {
            throw new EzException("Missing phone number");
        }

        User currentUser = userRepository.getUserByPhoneNumber(phoneNumber);
        UserInfo userInfo =  userMapper.userToUserInfo(currentUser);
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
}

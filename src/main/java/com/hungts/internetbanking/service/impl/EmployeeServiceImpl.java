package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.UserMapper;
import com.hungts.internetbanking.model.entity.User;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.repository.RoleRepository;
import com.hungts.internetbanking.repository.UserRepository;
import com.hungts.internetbanking.service.EmployeeService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.EncryptPasswordUtils;
import com.hungts.internetbanking.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserInfo createEmployee(UserRequest userRequest) {
        if (Utils.isEmpty(userRequest.getFullname()) || Utils.isEmpty(userRequest.getEmail())
                || Utils.isEmpty(userRequest.getPhone()) || Utils.isEmpty(userRequest.getPassword())) {
            throw new EzException("Missing field");
        }

        if (userService.findUserByEmail(userRequest.getEmail()) != null) {
            throw new EzException("User email is exists!");
        }

        if (userService.findUserByPhoneNumber(userRequest.getPhone()) != null) {
            throw new EzException("User phone number is exists");
        }

        String encryptPassword = EncryptPasswordUtils.encryptPassword(userRequest.getPassword());
        User insertUser = new User();
        insertUser.setEmail(userRequest.getEmail());
        insertUser.setPhone(userRequest.getPhone());
        insertUser.setFullName(userRequest.getFullname());
        insertUser.setPassword(encryptPassword);
        userRepository.insertUser(insertUser);

        roleRepository.saveUserRole(insertUser.getId(), Constant.UserRole.ROLE_EMPLOYEE);

        UserInfo userInfo = userMapper.userToUserInfo(insertUser);
        userInfo.setRole(Constant.UserRole.ROLE_EMPLOYEE);
        return userInfo;
    }

    @Override
    public UserInfo updateEmployee(UserRequest userRequest) {
        if (userRequest.getUserId() == null || userRequest.getUserId() <= 0) {
            throw new EzException("Mising user id");
        }

        User currentUser = userRepository.getUserById(userRequest.getUserId());
        if (currentUser == null) {
            throw new EzException("User does not exist");
        }

        if (StringUtils.isNotBlank(userRequest.getFullname())) {
            currentUser.setFullName(userRequest.getFullname());
        }

        if (StringUtils.isNotBlank(userRequest.getPassword())) {
            currentUser.setPassword(EncryptPasswordUtils.encryptPassword(userRequest.getPassword()));
        }

        userRepository.updateUser(currentUser);
        UserInfo userInfo = userMapper.userToUserInfo(currentUser);
        userInfo.setRole(Constant.UserRole.ROLE_EMPLOYEE);
        return userInfo;
    }

    @Override
    public void deleteEmployee(UserRequest userRequest) {
        if (userRequest.getUserId() == null || userRequest.getUserId() <= 0) {
            throw new EzException("Mising user id");
        }

        User currentUser = userRepository.getUserById(userRequest.getUserId());
        if (currentUser == null) {
            throw new EzException("User does not exist");
        }

        roleRepository.updateUserRole(currentUser.getId(), 4);
    }

    @Override
    public List<UserInfo> getListEmployee() {
        List<User> employeeList = userRepository.getListEmployee();
        return employeeList.stream().map(employee -> userMapper.userToUserInfo(employee)).collect(Collectors.toCollection(LinkedList::new));
    }
}

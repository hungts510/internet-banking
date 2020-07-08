package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.UserRequest;

import java.util.List;

public interface EmployeeService {
    UserInfo createEmployee(UserRequest userRequest);

    UserInfo updateEmployee(UserRequest userRequest);

    void deleteEmployee(UserRequest userRequest);

    List<UserInfo> getListEmployee();
}

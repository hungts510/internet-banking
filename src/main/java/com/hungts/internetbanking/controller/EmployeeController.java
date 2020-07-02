package com.hungts.internetbanking.controller;

import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = ContextPath.Employee.EMPLOYEE)
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = ContextPath.Employee.CREATE, method = RequestMethod.POST)
    public ResponseEntity<?> createEmployee(@RequestBody UserRequest userRequest) {
        UserInfo userInfo = employeeService.createEmployee(userRequest);

        ResponseBody responseBody = new ResponseBody(0, "Success", userInfo);
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = ContextPath.Employee.UPDATE, method = RequestMethod.POST)
    public ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest) {
        employeeService.updateEmployee(userRequest);

        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = ContextPath.Employee.LIST_EMPLOYEE, method = RequestMethod.GET)
    public ResponseEntity<?> getListEmployee() {
        List<UserInfo> employeeList = employeeService.getListEmployee();

        ResponseBody responseBody = new ResponseBody(0, "Success", employeeList);
        return EzResponse.response(responseBody);
    }
}

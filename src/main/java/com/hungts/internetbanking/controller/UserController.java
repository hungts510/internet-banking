package com.hungts.internetbanking.controller;

import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.UserRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ContextPath.User.USER, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = ContextPath.User.CREATE, method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);

        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @RequestMapping(value = ContextPath.User.INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getUserInfo(@RequestBody UserRequest userRequest) {
        if (Utils.isBlank(userRequest.getEmail()) && Utils.isBlank(userRequest.getPhone())) {
            throw new EzException("Missing field");
        }

        UserInfo userInfo = null;
        if (Utils.isNotEmpty(userRequest.getPhone())) {
            userInfo = userService.findUserByPhoneNumber(userRequest.getPhone());
        } else if (Utils.isNotEmpty(userRequest.getEmail())) {
            userInfo = userService.findUserByEmail(userRequest.getEmail());
        }

        ResponseBody responseBody = new ResponseBody();
        if (userInfo != null) {
            responseBody = new ResponseBody(0, "Success", userInfo);
        }

        return EzResponse.response(responseBody);
    }
}
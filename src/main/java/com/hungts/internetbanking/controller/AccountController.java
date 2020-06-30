package com.hungts.internetbanking.controller;

import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ContextPath.Account.ACCOUNT)
public class AccountController {
    @Autowired
    AccountService accountService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.Account.INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getAccountInfo(@RequestBody AccountRequest accountRequest) {
        if (accountRequest.getAccountNumber() == null || accountRequest.getAccountNumber() <= 0) {
            throw new EzException("Missing field account number");
        }

        AccountInfo accountInfo = accountService.getAccountInfoByAccountNumber(accountRequest.getAccountNumber());
        ResponseBody responseBody = new ResponseBody(0, "Success", accountInfo);
        return EzResponse.response(responseBody);
    }
}

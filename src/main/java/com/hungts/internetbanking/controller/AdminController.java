package com.hungts.internetbanking.controller;

import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.StatisticInfo;
import com.hungts.internetbanking.model.request.AdminViewRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.AccountService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = ContextPath.Admin.ADMIN)
public class AdminController {
    @Autowired
    AccountService accountService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = ContextPath.Admin.STATISTIC, method = RequestMethod.POST)
    public ResponseEntity<?> createEmployee(@RequestBody AdminViewRequest adminViewRequest) {
        if (ObjectUtils.isEmpty(adminViewRequest.getFromDate()) || ObjectUtils.isEmpty(adminViewRequest.getToDate())) {
            throw new EzException("Missing view time range");
        }

        StatisticInfo statisticInfo = accountService.bankStatistic(adminViewRequest.getFromDate(), adminViewRequest.getToDate(), adminViewRequest.getBankName());
        ResponseBody responseBody = new ResponseBody(0, "Success", statisticInfo);
        return EzResponse.response(responseBody);
    }
}

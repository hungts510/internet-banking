package com.hungts.internetbanking.controller;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.PartnerInfo;
import com.hungts.internetbanking.model.request.ExternalRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.PartnerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = ContextPath.Api.API + ContextPath.Api.ACCOUNT)
public class ExternalApiController {

    @Autowired
    PartnerService partnerService;

    @RequestMapping(value = ContextPath.Account.INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getAccountInfo(@RequestHeader Map<String, String> headers, @RequestBody ExternalRequest externalRequest) {
        if (externalRequest.getAccountNumber() == null || externalRequest.getAccountNumber() <= 0) {
            throw new EzException("Missing account number");
        }

        if (externalRequest.getRequestTime() == null || externalRequest.getRequestTime() <= 0) {
            throw new EzException("Missing request time");
        }

        String partnerCode = headers.get(Constant.ExternalRequest.Header.PARTNER_CODE);
        if (StringUtils.isBlank(partnerCode)) {
            throw new EzException("Missing partner code in request header");
        }

        String signature = headers.get(Constant.ExternalRequest.Header.SIGNATURE);
        if (StringUtils.isBlank(signature)) {
            throw new EzException("Missing signature in request header");
        }

        PartnerInfo partnerInfo =  partnerService.getPartnerByPartnerCode(partnerCode);
        //Get public key of partner
        //Verify request
        //Check request expired
        //Get account info
        //Sign and response

//        AccountInfo accountInfo = accountService.getAccountInfoByAccountNumber(accountRequest.getAccountNumber());
//        ResponseBody responseBody = new ResponseBody(0, "Success", accountInfo);
//        return EzResponse.response(responseBody);
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }
}

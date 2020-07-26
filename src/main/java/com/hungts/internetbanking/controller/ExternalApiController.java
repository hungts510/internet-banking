package com.hungts.internetbanking.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.PartnerInfo;
import com.hungts.internetbanking.model.info.ResponseExternalAccountInfo;
import com.hungts.internetbanking.model.request.ExternalRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.PartnerService;
import com.hungts.internetbanking.util.PGPSecurity;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Security;
import java.util.Map;

@RestController
@RequestMapping(value = ContextPath.Api.API + ContextPath.Api.ACCOUNT)
public class ExternalApiController {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Autowired
    PartnerService partnerService;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = ContextPath.Api.INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getAccountInfo(@RequestHeader Map<String, String> headers, @RequestBody ExternalRequest externalRequest) {
        String partnerCode = headers.get(Constant.ExternalRequest.Header.PARTNER_CODE);
        if (StringUtils.isBlank(partnerCode)) {
            throw new EzException("Missing partner code in request header");
        }

        PartnerInfo partnerInfo = partnerService.getPartnerByPartnerCode(partnerCode);
        if (partnerInfo == null) {
            throw new EzException("Partner does not exist");
        }

        AccountInfo accountInfo = null;
        PGPSecurity pgpSecurity = new PGPSecurity();
        String decryptedMessage = null;

        String publicKeyPartner = null;
        String partnerEmail = null;
        if (partnerInfo.getPartnerName().equals(Constant.PartnerName.BANK25)) {
            publicKeyPartner = Constant.BANK25_PGP_PUBLIC_KEY;
            partnerEmail = Constant.BANK25_USER_EMAIL;
        } else if (partnerInfo.getPartnerName().equals(Constant.PartnerName.BANK34)) {
            publicKeyPartner = Constant.BANK34_PGP_PUBLIC_KEYS;
            partnerEmail = Constant.BANK34_USER_EMAIL;
        }

        try {

            decryptedMessage = pgpSecurity.decryptAndVerify(externalRequest.getMessage(),
                    Constant.DEST_PASS_PHRASE,
                    PGPSecurity.ArmoredKeyPair.of(Constant.DEST_PRIVATE_KEYS, Constant.DEST_PUBLIC_KEYS),
                    partnerEmail,
                    publicKeyPartner);

            System.out.printf(decryptedMessage);
        } catch (Exception e) {
            throw new EzException("Message invalid: " + e.getMessage());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        ExternalRequest requestMessage = null;
        try {
            requestMessage = objectMapper.readValue(decryptedMessage, ExternalRequest.class);
        } catch (Exception e) {
            throw new EzException("Can't parse JSON message");
        }

        System.out.println("Request: " + requestMessage);

        if (requestMessage.getRequestTime() == null || requestMessage.getRequestTime() == 0
                || System.currentTimeMillis() - requestMessage.getRequestTime() > Constant.OTP_AVAILABLE_TIME) {
            throw new EzException("Request expired (5 minutes)");
        }

        if (requestMessage.getAccountNumber() == null || requestMessage.getAccountNumber() < 0) {
            throw new EzException("Missing account number");
        }

        accountInfo = accountService.getAccountInfoByAccountNumber(requestMessage.getAccountNumber());

        if (accountInfo == null) {
            throw new EzException("Account does not exist");
        }

        //Get public key of partner
        //Verify request
        //Check request expired
        //Get account info
        //Sign and response

        ResponseExternalAccountInfo externalAccountInfo = new ResponseExternalAccountInfo();
        externalAccountInfo.setAccountName(accountInfo.getAccountName());
        externalAccountInfo.setAccountNumber(accountInfo.getAccountNumber());
        externalAccountInfo.setAccountType("Tài khoản thanh toán");
        ResponseBody responseBody = new ResponseBody(0, "Success", externalAccountInfo);
        return EzResponse.response(responseBody);
    }

    @RequestMapping(value = ContextPath.Api.RECHARGE, method = RequestMethod.POST)
    public ResponseEntity<?> payInAccount(@RequestHeader Map<String, String> headers, @RequestBody ExternalRequest externalRequest) {
        String partnerCode = headers.get(Constant.ExternalRequest.Header.PARTNER_CODE);
        if (StringUtils.isBlank(partnerCode)) {
            throw new EzException("Missing partner code in request header");
        }

        PartnerInfo partnerInfo = partnerService.getPartnerByPartnerCode(partnerCode);
        if (partnerInfo == null) {
            throw new EzException("Partner does not exist");
        }

        AccountInfo accountInfo = null;

        if (partnerInfo.getPartnerType().equals(Constant.PartnerType.PGP)) {
            PGPSecurity pgpSecurity = new PGPSecurity();
            String decryptedMessage = null;

            try {

                decryptedMessage = pgpSecurity.decryptAndVerify(externalRequest.getMessage(),
                        Constant.DEST_PASS_PHRASE,
                        PGPSecurity.ArmoredKeyPair.of(Constant.DEST_PRIVATE_KEYS, Constant.DEST_PUBLIC_KEYS),
                        Constant.SOURCE_USER_EMAIL,
                        Constant.SOURCE_PUBLIC_KEYS);

                System.out.printf(decryptedMessage);
            } catch (Exception e) {
                throw new EzException("Message invalid: " + e.getMessage());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            ExternalRequest requestMessage = null;
            try {
                requestMessage = objectMapper.readValue(decryptedMessage, ExternalRequest.class);
            } catch (Exception e) {
                throw new EzException("Can't parse JSON message");
            }

            if (requestMessage.getRequestTime() == null || requestMessage.getRequestTime() == 0
                    || System.currentTimeMillis() - requestMessage.getRequestTime() > Constant.OTP_AVAILABLE_TIME) {
                throw new EzException("Request expired (5 minutes)");
            }

            if (requestMessage.getAccountNumber() == null || requestMessage.getAccountNumber() < 0) {
                throw new EzException("Missing account number");
            }

            if (requestMessage.getAmount() == null || requestMessage.getAmount() < 0) {
                throw new EzException("Missing amount");
            }
        }
        //Get public key of partner
        //Verify request
        //Check request expired
        //Get account info
        //Sign and response

//        ResponseExternalAccountInfo externalAccountInfo = new ResponseExternalAccountInfo();
//        externalAccountInfo.setAccountName(accountInfo.getAccountName());
//        externalAccountInfo.setAccountNumber(accountInfo.getAccountNumber());
//        externalAccountInfo.setAccountType("Tài khoản thanh toán");
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @RequestMapping(value = ContextPath.Api.GENERATE_MESSAGE, method = RequestMethod.POST)
    public ResponseEntity<?> getAccountInfo(@RequestBody ExternalRequest externalRequest) {
        PGPSecurity pgpSecurity = new PGPSecurity();
        String encryptMessage = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String requestMessage = objectMapper.writeValueAsString(externalRequest);

            encryptMessage = pgpSecurity.encryptAndSign(requestMessage,
                    Constant.DEST_USER_EMAIL,
                    Constant.DEST_PASS_PHRASE,
                    PGPSecurity.ArmoredKeyPair.of(Constant.DEST_PRIVATE_KEYS, Constant.DEST_PUBLIC_KEYS),
                    Constant.BANK34_USER_EMAIL,
                    Constant.BANK25_PGP_PUBLIC_KEY);

            System.out.printf(encryptMessage);
        } catch (Exception e) {
            throw new EzException("Message invalid: " + e.getMessage());
        }


        //Get public key of partner
        //Verify request
        //Check request expired
        //Get account info
        //Sign and response

        ResponseBody responseBody = new ResponseBody(0, "Success", encryptMessage);
        return EzResponse.response(responseBody);
    }
}

package com.hungts.internetbanking.controller;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.define.ContextPath;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.entity.Partner;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.PartnerInfo;
import com.hungts.internetbanking.model.info.TransactionInfo;
import com.hungts.internetbanking.model.info.TransactionMetaData;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.model.request.TransactionRequest;
import com.hungts.internetbanking.model.response.EzResponse;
import com.hungts.internetbanking.model.response.ResponseBody;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.PartnerService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ObjectInput;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(ContextPath.Account.ACCOUNT)
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    PartnerService partnerService;

    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_EMPLOYEE')")
    @RequestMapping(value = ContextPath.Account.INFO, method = RequestMethod.POST)
    public ResponseEntity<?> getAccountInfo(@RequestBody AccountRequest accountRequest) {
        if (accountRequest.getAccountNumber() == null || accountRequest.getAccountNumber() <= 0) {
            throw new EzException("Missing field account number");
        }

        if (ObjectUtils.isEmpty(accountRequest.getAccountBank())) {
            throw new EzException("Missing account bank");
        }

        AccountInfo accountInfo = null;
        if (accountRequest.getAccountBank().equals(Constant.BANK_NAME)) {
            accountInfo = accountService.getAccountInfoByAccountNumber(accountRequest.getAccountNumber());
        } else {
            PartnerInfo partner = partnerService.getPartnerByPartnerName(accountRequest.getAccountBank());
            if (partner.getPartnerType().equals(Constant.PartnerType.RSA)) {

            }
        }
        ResponseBody responseBody = new ResponseBody(0, "Success", accountInfo);
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @RequestMapping(value = ContextPath.Account.PAY_IN, method = RequestMethod.POST)
    public ResponseEntity<?> payInToAccount(@RequestBody TransactionRequest transactionRequest) {
        if (transactionRequest.getToAccountNumber() == null || transactionRequest.getToAccountNumber() <= 0) {
            throw new EzException("Missing to account number");
        }

        if (transactionRequest.getAmount() == null || transactionRequest.getAmount() <= 0) {
            throw new EzException("Missing transaction amount");
        }

        accountService.payInToAccount(transactionRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.Account.TRANSFER, method = RequestMethod.POST)
    public ResponseEntity<?> transferMoney(@RequestBody TransactionRequest transactionRequest) {
        if (transactionRequest.getFromAccountNumber() == null || transactionRequest.getFromAccountNumber() <= 0) {
            throw new EzException("Missing source account number");
        }

        if (transactionRequest.getToAccountNumber() == null || transactionRequest.getToAccountNumber() <= 0) {
            throw new EzException("Missing destination account number");
        }

        if (transactionRequest.getAmount() == null || transactionRequest.getAmount() <= 0) {
            throw new EzException("Missing transaction amount");
        }

        if (StringUtils.isBlank(transactionRequest.getFromBank()) || StringUtils.isBlank(transactionRequest.getToBank())
                || !transactionRequest.getFromBank().equals(transactionRequest.getToBank()) || !transactionRequest.getFromBank().equals(Constant.BANK_NAME)
                || !transactionRequest.getToBank().equals(Constant.BANK_NAME)) {
            throw new EzException("API just for internal transfer purpose");
        }

        TransactionInfo transactionInfo = accountService.createTransferMoneyTransaction(transactionRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success", transactionInfo);
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.Account.CAPTURE_TRANSFER, method = RequestMethod.POST)
    public ResponseEntity<?> captureTransfer(@RequestBody TransactionRequest transactionRequest) {
        if (transactionRequest.getTransactionId() == null || transactionRequest.getTransactionId() <= 0) {
            throw new EzException("Missing transaction id");
        }

        if (StringUtils.isBlank(transactionRequest.getOtp()) || Integer.parseInt(transactionRequest.getOtp()) <= 0) {
            throw new EzException("Missing destination account number");
        }

        accountService.captureTransferTransaction(transactionRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success");
        return EzResponse.response(responseBody);
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_CUSTOMER')")
    @RequestMapping(value = ContextPath.Account.LIST_TRANSACTIONS, method = RequestMethod.POST)
    public ResponseEntity<?> listTransactions(@RequestBody AccountRequest accountRequest) {
        if (accountRequest.getAccountNumber() == null || accountRequest.getAccountNumber() <= 0) {
            throw new EzException("Missing account number");
        }

        if (accountRequest.getTransactionType() == null || accountRequest.getTransactionType() <= 0) {
            throw new EzException("Missing transaction type");
        }

        TransactionMetaData transactionMetaData = accountService.getListAccountTransaction(accountRequest);
        ResponseBody responseBody = new ResponseBody(0, "Success", transactionMetaData);
        return EzResponse.response(responseBody);
    }
}

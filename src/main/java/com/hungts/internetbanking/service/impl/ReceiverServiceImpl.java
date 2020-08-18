package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.define.Constant;
import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.mapper.ReceiverMapper;
import com.hungts.internetbanking.model.entity.Receiver;
import com.hungts.internetbanking.model.info.AccountInfo;
import com.hungts.internetbanking.model.info.ReceiverInfo;
import com.hungts.internetbanking.model.info.ResponseExternalAccountInfo;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.repository.ReceiverRepository;
import com.hungts.internetbanking.service.AccountService;
import com.hungts.internetbanking.service.ReceiverService;
import com.hungts.internetbanking.service.UserService;
import com.hungts.internetbanking.util.VNCharacterUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiverServiceImpl implements ReceiverService {
    @Autowired
    ReceiverRepository receiverRepository;

    @Autowired
    UserService userService;

    @Autowired
    ReceiverMapper receiverMapper;

    @Autowired
    AccountService accountService;

    @Override
    public void createReceiver(AccountRequest accountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User not exist");
        }

        ResponseExternalAccountInfo responseExternalAccountInfo = null;
        AccountInfo accountInfo = null;
        if (accountRequest.getAccountBank().equals(Constant.BANK_NAME)) {
            accountInfo = accountService.getAccountInfoByAccountNumber(accountRequest.getAccountNumber());
            if (accountInfo == null) {
                throw new EzException("Account does not exist");
            }
        } else if (accountRequest.getAccountBank().equals(Constant.PartnerName.BANK34)) {
            responseExternalAccountInfo = accountService.getPGPAccountInfo(accountRequest.getAccountBank(), accountRequest.getAccountNumber());
        } else {
            responseExternalAccountInfo = accountService.getRSAAccountInfo(accountRequest.getAccountBank(), accountRequest.getAccountNumber());
        }

        if (accountInfo == null && responseExternalAccountInfo == null) {
            throw new EzException("Account does not exist");
        }

        if (StringUtils.isEmpty(accountRequest.getAccountName())) {
            if (accountInfo != null) {
                UserInfo receiverInfo = userService.getUserById(accountInfo.getUserId());
                accountRequest.setAccountName(receiverInfo.getFullName());
            } else {
                accountRequest.setAccountName(responseExternalAccountInfo.getAccountName());
            }
        }

        Date currentDate = new Date();
        Receiver receiver = new Receiver();
        receiver.setUserId(userInfo.getUserId());
        receiver.setReceiverAccountNumber(accountRequest.getAccountNumber());
        receiver.setReceiverName(VNCharacterUtils.removeAccent(accountRequest.getAccountName()));
        receiver.setReceiverBank(accountRequest.getAccountBank());
        receiver.setCreatedAt(currentDate);
        receiver.setUpdatedAt(currentDate);

        receiverRepository.saveReceiver(receiver);
    }

    @Override
    public List<ReceiverInfo> getListReceiverByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User not exist");
        }

        List<Receiver> receiverList = receiverRepository.getListReceiverByUserId(userInfo.getUserId());
        return receiverList.stream().map(receiver -> receiverMapper.receiverToReceiverInfo(receiver)).collect(Collectors.toList());
    }

    @Override
    public void updateReceiver(AccountRequest accountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User not exist");
        }

        Receiver currentReceiver = receiverRepository.getReceiverByUserIdAndAccountNumber(userInfo.getUserId(), accountRequest.getAccountNumber());
        if (currentReceiver == null) {
            throw new EzException("Receiver not exist");
        }

        receiverRepository.updateReceiver(currentReceiver.getId(), accountRequest.getAccountName(), new Date());
    }

    @Override
    public void removeReceiver(AccountRequest accountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User not exist");
        }

        Receiver currentReceiver = receiverRepository.getReceiverByUserIdAndAccountNumber(userInfo.getUserId(), accountRequest.getAccountNumber());
        if (currentReceiver == null) {
            throw new EzException("Receiver not exist");
        }

        receiverRepository.deleteReceiver(currentReceiver.getId());
    }
}

package com.hungts.internetbanking.service.impl;

import com.hungts.internetbanking.exception.EzException;
import com.hungts.internetbanking.model.entity.Receiver;
import com.hungts.internetbanking.model.info.UserInfo;
import com.hungts.internetbanking.model.request.AccountRequest;
import com.hungts.internetbanking.repository.ReceiverRepository;
import com.hungts.internetbanking.service.ReceiverService;
import com.hungts.internetbanking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReceiverServiceImpl implements ReceiverService {
    @Autowired
    ReceiverRepository receiverRepository;

    @Autowired
    UserService userService;

    @Override
    public void createReceiver(AccountRequest accountRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();

        UserInfo userInfo = userService.findUserByPhoneNumber(phoneNumber);
        if (userInfo == null) {
            throw new EzException("User not exist");
        }

        Date currentDate = new Date();
        Receiver receiver = new Receiver();
        receiver.setUserId(userInfo.getUserId());
        receiver.setReceiverAccountNumber(accountRequest.getAccountNumber());
        receiver.setReceiverName(accountRequest.getAccountName());
        receiver.setReceiverBank(accountRequest.getAccountBank());
        receiver.setCreatedAt(currentDate);
        receiver.setUpdatedAt(currentDate);

        receiverRepository.saveReceiver(receiver);
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
}

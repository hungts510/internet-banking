package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.ReceiverInfo;
import com.hungts.internetbanking.model.request.AccountRequest;

import java.util.List;

public interface ReceiverService {
    void createReceiver(AccountRequest accountRequest);

    List<ReceiverInfo> getListReceiverByUser();

    void updateReceiver(AccountRequest accountRequest);

    void removeReceiver(AccountRequest accountRequest);
}

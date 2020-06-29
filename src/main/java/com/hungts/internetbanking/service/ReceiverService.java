package com.hungts.internetbanking.service;

import com.hungts.internetbanking.model.info.ReceiverInfo;
import com.hungts.internetbanking.model.request.AccountRequest;

public interface ReceiverService {
    void createReceiver(AccountRequest accountRequest);

    void updateReceiver(AccountRequest accountRequest);
}

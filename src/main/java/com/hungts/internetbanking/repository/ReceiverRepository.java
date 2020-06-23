package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Receiver;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface ReceiverRepository {
    @Insert("INSERT INTO receiver(user_id, receiver_bank, receiver_account_number, receiver_name, created_at, updated_at) VALUES" +
            "(#{userId},  #{receiverBank}, #{receiverAccountNumber}, #{receiverName}, #{createdAt}, #{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveReceiver(Receiver receiver);
}

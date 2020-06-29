package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Receiver;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Mapper
public interface ReceiverRepository {
    @Insert("INSERT INTO receiver(user_id, receiver_bank, receiver_account_number, receiver_name, created_at, updated_at) VALUES" +
            "(#{userId},  #{receiverBank}, #{receiverAccountNumber}, #{receiverName}, #{createdAt}, #{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveReceiver(Receiver receiver);

    @Select("SELECT * FROM receiver WHERE user_id = #{userId} AND receiver_account_number = #{receiverAccountNumber}")
    @Results(
            id = "ReceiverObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "receiver_bank", property = "receiverBank"),
            @Result(column = "receiver_account_number", property = "receiverAccountNumber"),
            @Result(column = "receiver_name", property = "receiverName"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    Receiver getReceiverByUserIdAndAccountNumber(int userId, long receiverAccountNumber);

    @Update("UPDATE receiver SET receiver_name = #{receiverName}, updated_at = #{updatedAt} WHERE id = #{receiverId}")
    void updateReceiver(int receiverId, String receiverName, Date updatedAt);
}

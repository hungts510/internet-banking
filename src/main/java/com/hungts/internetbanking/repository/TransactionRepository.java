package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Transaction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Mapper
@Repository
public interface TransactionRepository {
    @Insert("INSERT INTO transactions(from_user_id, to_user_id, from_account_number, to_account_number, amount, type, status, description, created_at, updated_at, from_bank, to_bank, otp) VALUES" +
            "(#{fromUserId}, #{toUserId}, #{fromAccountNumber}, #{toAccountNumber}, #{amount}, #{type}, #{status}, #{description}, #{createdAt}, #{updatedAt}, #{fromBank}, #{toBank}, #{otp})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveTransaction(Transaction transaction);

    @Select("SELECT * FROM transactions WHERE id = #{transactionId} AND from_user_id = #{userId} AND status = #{status}")
    @Results(
            id = "TransactionObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "from_user_id", property = "fromUserId"),
            @Result(column = "to_user_id", property = "toUserId"),
            @Result(column = "from_account_number", property = "fromAccountNumber"),
            @Result(column = "to_account_number", property = "toAccountNumber"),
            @Result(column = "amount", property = "amount"),
            @Result(column = "type", property = "type"),
            @Result(column = "status", property = "status"),
            @Result(column = "description", property = "description"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "from_bank", property = "fromBank"),
            @Result(column = "to_bank", property = "toBank"),
            @Result(column = "otp", property = "otp"),
    })
    Transaction getUserTransactionByIdAndStatus(int transactionId, int userId, int status);

    @Update("UPDATE transactions SET status = #{status} WHERE id = #{transactionId}")
    void updateTransactionStatus(int status, int transactionId);

    @Select("SELECT * FROM transactions WHERE type = #{type} AND from_account_number = #{fromAccountNumber} ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByTypeFromAccount(int type, long fromAccountNumber);

    @Select("SELECT * FROM transactions WHERE to_account_number = #{toAccountNumber} ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByTypeToAccount(long toAccountNumber);
}

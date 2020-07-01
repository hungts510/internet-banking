package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TransactionRepository {
    @Insert("INSERT INTO transactions(from_user_id, to_user_id, from_account_number, to_account_number, amount, type, status, description, created_at, updated_at, from_bank, to_bank) VALUES" +
            "(#{fromUserId}, #{toUserId}, #{fromAccountNumber}, #{toAccountNumber}, #{amount}, #{type}, #{status}, #{description}, #{createdAt}, #{updatedAt}, #{fromBank}, #{toBank})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveTransaction(Transaction transaction);
}

package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.TransactionSignature;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TransactionSignatureRepository {
    @Insert("INSERT INTO transaction_signature(transaction_id, signature, created_at, updated_at) VALUES (#{transactionId}, #{signature}, #{createdAt}, #{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveTransactionSignature(TransactionSignature transactionSignature);
}

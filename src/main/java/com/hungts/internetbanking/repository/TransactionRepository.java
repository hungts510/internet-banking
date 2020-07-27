package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Transaction;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface TransactionRepository {
    @Insert("INSERT INTO transactions(from_user_id, to_user_id, from_account_number, to_account_number, amount, type, status, description, created_at, updated_at, from_bank, to_bank, otp, debt_id, is_pay_fee) VALUES" +
            "(#{fromUserId}, #{toUserId}, #{fromAccountNumber}, #{toAccountNumber}, #{amount}, #{type}, #{status}, #{description}, #{createdAt}, #{updatedAt}, #{fromBank}, #{toBank}, #{otp}, #{debtId}, #{isPayFee})")
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
            @Result(column = "debt_id", property = "debtId"),
            @Result(column = "is_pay_fee", property = "isPayFee")
    })
    Transaction getUserTransactionByIdAndStatus(int transactionId, int userId, int status);

    @Update("UPDATE transactions SET status = #{status} WHERE id = #{transactionId}")
    void updateTransactionStatus(int status, int transactionId);

    @Select("SELECT * FROM transactions WHERE type = #{type} AND from_account_number = #{fromAccountNumber} AND status != 2 ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByTypeFromAccount(int type, long fromAccountNumber);

    @Select("SELECT * FROM transactions WHERE type = #{type} AND to_account_number = #{toAccountNumber} AND status != 2 ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByTypeToAccount(int type, long toAccountNumber);

    @Select("SELECT * FROM transactions WHERE status = #{status} AND to_bank LIKE '%' #{bankName} '%' AND created_at >= #{dateFrom} AND created_at <= #{dateTo} ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByStatusToBank(int status, String bankName, Date dateFrom, Date dateTo);

    @Select("SELECT * FROM transactions WHERE status = #{status} AND from_bank LIKE '%' #{bankName} '%' AND created_at >= #{dateFrom} AND created_at <= #{dateTo} ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByStatusFromBank(int status, String bankName, Date dateFrom, Date dateTo);

    @Select("SELECT * FROM transactions WHERE status = #{status} AND to_bank NOT LIKE '%' #{bankName} '%' AND created_at >= #{dateFrom} AND created_at <= #{dateTo} ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByStatusToExternalBank(int status, String bankName, Date dateFrom, Date dateTo);

    @Select("SELECT * FROM transactions WHERE status = #{status} AND from_bank NOT LIKE '%' #{bankName} '%' AND created_at >= #{dateFrom} AND created_at <= #{dateTo} ORDER BY created_at DESC")
    @ResultMap("TransactionObject")
    List<Transaction> getListTransactionByStatusFromExternalBank(int status, String bankName, Date dateFrom, Date dateTo);
}

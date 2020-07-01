package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Account;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccountRepository {
    @Insert("INSERT INTO account(account_number, account_type, balance, user_id, created_at, updated_at) " +
            "VALUES (#{accountNumber}, #{accountType}, #{balance}, #{userId}, #{createdAt}, #{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveAccount(Account account);

    @Select("SELECT * FROM account WHERE id = #{id}")
    @Results(
            id = "AccountObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "account_number", property = "accountNumber"),
            @Result(column = "account_type", property = "accountType"),
            @Result(column = "balance", property = "balance"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    Account getAccountById(int id);

    @Select("SELECT * FROM account WHERE user_id = #{userId}")
    @ResultMap("AccountObject")
    List<Account> getAllAccountByUserId(int userId);

    @Select("SELECT id, account_number, account_type, user_id, created_at, updated_at FROM account WHERE account_number = #{accountNumber}")
    @Results(
            id = "AccountInfo", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "account_number", property = "accountNumber"),
            @Result(column = "account_type", property = "accountType"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    Account getCustomerAccountByAccountNumber(long accountNumber);

    @Select("SELECT * FROM account WHERE user_id = #{userId} AND account_type = #{accountType}")
    @ResultMap("AccountObject")
    Account getUserAccountByType(int userId, int accountType);

    @Update("UPDATE account SET balance = #{newBalance} WHERE id = #{accountId}")
    void updateAccountBalance(long newBalance, int accountId);
}

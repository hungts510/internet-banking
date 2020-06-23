package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Account;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

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
}

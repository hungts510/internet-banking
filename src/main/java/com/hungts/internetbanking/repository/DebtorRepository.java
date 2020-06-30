package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Debtor;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DebtorRepository {
    @Insert("INSERT INTO debtor(user_id, debtor_account_number, amount, description, created_at, updated_at, status) VALUES" +
            "(#{userId}, #{debtorAccountNumber}, #{amount}, #{description}, #{createdAt}, #{updatedAt}, #{status})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveDebtor(Debtor debtor);

    @Select("SELECT * FROM debtor WHERE id = #{debtorId}")
    @Results(
            id = "DebtorObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "debtor_account_number", property = "debtorAccountNumber"),
            @Result(column = "amount", property = "amount"),
            @Result(column = "description", property = "description"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "status", property = "status")
    })
    Debtor getDebtorById(int debtorId);
}

package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.ResetPasswordRequest;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ResetPasswordRepository {

    @Insert("INSERT INTO reset_password_request(user_id, otp, created_at, status) VALUES (#{userId}, #{otp}, #{createdAt}, #{status})")
    void saveResetPasswordRequest(ResetPasswordRequest resetPasswordRequest);

    @Select("SELECT * FROM reset_password_request WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 1")
    @Results(
            id = "ResetPasswordObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "otp", property = "otp"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "status", property = "status")
    })
    ResetPasswordRequest getLastResetPasswordRequestByUserId(int userId);
}

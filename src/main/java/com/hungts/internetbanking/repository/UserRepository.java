package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {
    @Insert("INSERT INTO user(fullname, email, phone, password) VALUES(#{fullName}, #{email}, #{phone}, #{password})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User getUserByEmail(String email);

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User getUserByPhoneNumber(String phone);

    @Update("UPDATE user SET password = #{newPassword} WHERE id = #{userId}")
    void updatePassword(int userId, String newPassword);
}

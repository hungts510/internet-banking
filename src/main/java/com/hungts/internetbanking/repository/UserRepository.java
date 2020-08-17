package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRepository {
    @Insert("INSERT INTO user(fullname, email, phone, password) VALUES(#{fullName}, #{email}, #{phone}, #{password})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void insertUser(User user);

    @Select("SELECT * FROM user WHERE email = #{email}")
    @ResultMap("UserObject")
    User getUserByEmail(String email);

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    @ResultMap("UserObject")
    User getUserByPhoneNumber(String phone);

    @Update("UPDATE user SET password = #{newPassword} WHERE id = #{userId}")
    void updatePassword(int userId, String newPassword);

    @Select("SELECT user.* FROM user, user_role WHERE user.id = user_role.user_id AND role_id = 2")
    List<User> getListEmployee();

    @Select("SELECT * FROM user WHERE id = #{userId}")
    @Results(
            id = "UserObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "full_name", property = "fullName"),
            @Result(column = "password", property = "password"),
            @Result(column = "email", property = "email"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "refresh_token", property = "refreshToken"),
    })
    User getUserById(int userId);

    @Update("UPDATE user SET fullname = #{fullName}, password = #{password} WHERE id = #{id}")
    void updateUser(User user);

    @Update("UPDATE user SET refresh_token = #{refreshToken} WHERE phone = #{phone}")
    void saveRefreshToken(String refreshToken, String phone);
}

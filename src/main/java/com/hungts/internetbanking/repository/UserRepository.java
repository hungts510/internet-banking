package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {
    @Insert("INSERT INTO user(fullname, email, phone, password) VALUES(#{fullName}, #{email}, #{phone}, #{password})")
    void insertUser(@Param("fullName") String fullName, String email, String phone, String password);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User getUserByEmail(String email);

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User getUserByPhoneNumber(String phone);
}

package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {
    @Insert("INSERT INTO user(fullname, email, phone) VALUES(#{fullName}, #{email}, #{phone})")
    void insertUser(String fullName, String email, String phone);

    @Select("SELECT * FROM user WHERE email = #{email}")
    User getUserByEmail(String email);
}

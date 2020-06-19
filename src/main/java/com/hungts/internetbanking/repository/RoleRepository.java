package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoleRepository {
    @Select("SELECT r.* FROM role r, user_role ur WHERE r.id = ur.role_id AND ur.user_id = #{userId}")
    @Results(id = "RoleObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "role_name", property = "roleName")
    })
    Role getRoleFromUserId(Integer userId);
}

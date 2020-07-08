package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Role;
import org.apache.ibatis.annotations.*;
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

    @Insert("INSERT INTO user_role(user_id, role_id) VALUES(#{userId}, #{roleId})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveUserRole(int userId, int roleId);

    @Update("UPDATE user_role SET role_id = #{roleId} WHERE user_id = #{userId}")
    void updateUserRole(int userId, int roleId);
}

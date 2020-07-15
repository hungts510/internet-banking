package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Notification;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NotificationRepository {
    @Insert("INSERT INTO notification(from_user_id, to_user_id, content, status, debtor_id, created_at, updated_at)" +
            "VALUES (#{fromUserId}, #{toUserId}, #{content}, #{status}, #{debtorId}, #{createdAt}, #{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Integer.class)
    void saveNotification(Notification notification);

    @Select("SELECT * FROM notification WHERE to_user_id = #{userId} ORDER BY created_at DESC")
    @Results(
            id = "NotificationObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "from_user_id", property = "fromUserId"),
            @Result(column = "to_user_id", property = "toUserId"),
            @Result(column = "content", property = "content"),
            @Result(column = "status", property = "status"),
            @Result(column = "debtor_id", property = "debtorId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    List<Notification> getListUserNotification(Integer userId);

    @Select("SELECT * FROM notification WHERE id = #{notificationId}")
    @ResultMap("NotificationObject")
    Notification getNotificationById(Integer notificationId);

    @Update("UPDATE notification SET status = #{status} WHERE id = #{notificationId}")
    void updateNotificationStatus(Integer status, Integer notificationId);

    @Update("UPDATE notification SET status = #{status} WHERE to_user_id = #{userId}")
    void updateAllUserNotificationStatus(Integer status, Integer userId);
}

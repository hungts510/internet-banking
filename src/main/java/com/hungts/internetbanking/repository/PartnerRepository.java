package com.hungts.internetbanking.repository;

import com.hungts.internetbanking.model.entity.Partner;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PartnerRepository {

    @Select("SELECT * FROM partner WHERE partner_code = #{partnerCode}")
    @Results(
            id = "PartnerObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "partner_name", property = "partnerName"),
            @Result(column = "partner_code", property = "partnerCode"),
            @Result(column = "partner_type", property = "partnerType"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt")
    })
    Partner getPartnerByPartnerCode(String partnerCode);

    @Select("SELECT * FROM partner WHERE partner_name = #{partnerName}")
    @ResultMap("PartnerObject")
    Partner getPartnerByPartnerName(String partnerName);

    @Select("SELECT * FROM partner")
    @ResultMap("PartnerObject")
    List<Partner> getListPartners();
}

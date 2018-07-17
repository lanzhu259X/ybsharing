package com.yiban.sharing.dao;

import com.yiban.sharing.entities.UserAuth;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuthMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAuth record);

    UserAuth selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(UserAuth record);

    UserAuth findByIdentifier(String name, String openId);
}
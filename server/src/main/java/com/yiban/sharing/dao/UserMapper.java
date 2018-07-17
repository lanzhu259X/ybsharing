package com.yiban.sharing.dao;

import com.yiban.sharing.entities.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insert(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(User record);

    User findByPhone(String username);
}
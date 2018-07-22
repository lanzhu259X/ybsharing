package com.yiban.sharing.dao;

import com.yiban.sharing.entities.SharingProcess;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SharingProcessMapper {

    int insert(SharingProcess record);

    SharingProcess selectByPrimaryKey(Integer id);

    List<SharingProcess> getByRecordId(Integer recordId);

    int updateByPrimaryKey(SharingProcess record);
}
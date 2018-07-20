package com.yiban.sharing.dao;

import com.yiban.sharing.dto.SharingQuery;
import com.yiban.sharing.entities.SharingModal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SharingModalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SharingModal record);

    SharingModal selectByPrimaryKey(Integer id);

    SharingModal findBySharingNo(String sharingNo);

    int updateByPrimaryKey(SharingModal record);

    int sharingListCount(SharingQuery query);

    List<SharingModal> sharingList(SharingQuery query);
}
package com.yiban.sharing.dao;

import com.yiban.sharing.entities.SharingCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SharingCountMapper {

    int insert(SharingCount record);

    SharingCount selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SharingCount record);

    SharingCount getBySharingNo(String sharingNo);

    int updateBySharingNo(@Param("sharingNo") String sharingNo,
                          @Param("totalClick") long totalClick);
}
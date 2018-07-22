package com.yiban.sharing.dao;

import com.yiban.sharing.dto.SharingQuery;
import com.yiban.sharing.entities.SharingRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SharingRecordMapper {

    int insert(SharingRecord record);

    SharingRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SharingRecord record);

    int getSharingRecordsCount(String sharingNo);

    List<SharingRecord> getSharingRecords(SharingQuery query);

    SharingRecord getByRecordTicket(String recordTicket);
}
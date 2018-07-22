package com.yiban.sharing.entities;

import java.util.Date;

public class SharingCount {
    private Integer id;

    private String sharingNo;

    private Long totalClick;

    private Long totalRecord;

    private Long totalUnprocess;

    private Date updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSharingNo() {
        return sharingNo;
    }

    public void setSharingNo(String sharingNo) {
        this.sharingNo = sharingNo == null ? null : sharingNo.trim();
    }

    public Long getTotalClick() {
        return totalClick;
    }

    public void setTotalClick(Long totalClick) {
        this.totalClick = totalClick;
    }

    public Long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Long getTotalUnprocess() {
        return totalUnprocess;
    }

    public void setTotalUnprocess(Long totalUnprocess) {
        this.totalUnprocess = totalUnprocess;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
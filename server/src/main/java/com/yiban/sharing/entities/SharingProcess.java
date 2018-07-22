package com.yiban.sharing.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SharingProcess {
    private Integer id;

    private Integer recordId;

    private Date processTime;

    private Integer processUser;

    private String processUserName;

    private String processResult;

    private Date createdTime;

    private String createdBy;

    private String handleStatus;

    public String getShowTitle() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append(processUserName);
        sb.append(" [");
        sb.append(format.format(processTime));
        sb.append("]");
        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Integer getProcessUser() {
        return processUser;
    }

    public void setProcessUser(Integer processUser) {
        this.processUser = processUser;
    }

    public String getProcessUserName() {
        return processUserName;
    }

    public void setProcessUserName(String processUserName) {
        this.processUserName = processUserName == null ? null : processUserName.trim();
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult == null ? null : processResult.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }
}
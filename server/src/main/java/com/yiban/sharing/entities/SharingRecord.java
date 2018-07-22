package com.yiban.sharing.entities;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

public class SharingRecord {
    private Integer id;

    private String recordTicket;

    private Integer modalId;

    private String sharingNo;

    private String handleStatus;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

    private String recordModal;

    private List<ModalBody> modalBodyList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecordTicket() {
        return recordTicket;
    }

    public void setRecordTicket(String recordTicket) {
        this.recordTicket = recordTicket;
    }

    public Integer getModalId() {
        return modalId;
    }

    public void setModalId(Integer modalId) {
        this.modalId = modalId;
    }

    public String getSharingNo() {
        return sharingNo;
    }

    public void setSharingNo(String sharingNo) {
        this.sharingNo = sharingNo == null ? null : sharingNo.trim();
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus == null ? null : handleStatus.trim();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getRecordModal() {
        return recordModal;
    }

    public void setRecordModal(String recordModal) {
        this.recordModal = recordModal == null ? null : recordModal.trim();
    }

    public List<ModalBody> getModalBodyList() {
        if (this.recordModal != null && !StringUtils.isEmpty(this.recordModal)) {
            return JSONArray.parseArray(this.recordModal, ModalBody.class);
        }else {
            return modalBodyList;
        }
    }

    public void setModalBodyList(List<ModalBody> modalBodyList) {
        this.modalBodyList = modalBodyList;
        if (modalBodyList != null && !modalBodyList.isEmpty()) {
            this.setRecordModal(JSON.toJSONString(modalBodyList));
        }
    }
}
package com.yiban.sharing.entities;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

public class SharingModal {
    private Integer id;

    private String sharingNo;

    private String status;

    private String sharingTitle;

    private String sharingSubTitle;

    private String sharingContent;

    private String headImage;

    private String headFileKey;

    private String recordModal;

    private List<ModalBody> modalBodyList;

    private String createdBy;

    private String updatedBy;

    private Date createdTime;

    private Date updatedTime;

    private String recordTicket; //用于分享时每次获取页面的信息的时候控制职能提交一次.

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getSharingTitle() {
        return sharingTitle;
    }

    public void setSharingTitle(String sharingTitle) {
        this.sharingTitle = sharingTitle == null ? null : sharingTitle.trim();
    }

    public String getSharingSubTitle() {
        return sharingSubTitle;
    }

    public void setSharingSubTitle(String sharingSubTitle) {
        this.sharingSubTitle = sharingSubTitle == null ? null : sharingSubTitle.trim();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy == null ? null : updatedBy.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getSharingContent() {
        return sharingContent;
    }

    public void setSharingContent(String sharingContent) {
        this.sharingContent = sharingContent;
    }

    public String getRecordModal() {
        return recordModal;
    }

    public void setRecordModal(String recordModal) {
        this.recordModal = recordModal;
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

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getRecordTicket() {
        return recordTicket;
    }

    public void setRecordTicket(String recordTicket) {
        this.recordTicket = recordTicket;
    }

    public String getHeadFileKey() {
        return headFileKey;
    }

    public void setHeadFileKey(String headFileKey) {
        this.headFileKey = headFileKey;
    }
}
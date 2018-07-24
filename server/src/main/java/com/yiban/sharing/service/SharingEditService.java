package com.yiban.sharing.service;

import com.alibaba.fastjson.JSON;
import com.yiban.sharing.constants.SharingHandleStatus;
import com.yiban.sharing.constants.SharingModalStatus;
import com.yiban.sharing.dao.SharingCountMapper;
import com.yiban.sharing.dao.SharingModalMapper;
import com.yiban.sharing.dao.SharingProcessMapper;
import com.yiban.sharing.dao.SharingRecordMapper;
import com.yiban.sharing.dto.SharingQuery;
import com.yiban.sharing.entities.*;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.exception.ErrorCode;
import com.yiban.sharing.utils.AliOSSClientUtil;
import com.yiban.sharing.utils.EmailClientUtil;
import com.yiban.sharing.utils.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SharingEditService {

    @Value("${notify.record.emails}")
    private String recordEmails;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SharingModalMapper sharingModalMapper;
    @Autowired
    private SharingRecordMapper sharingRecordMapper;
    @Autowired
    private SharingProcessMapper sharingProcessMapper;
    @Autowired
    private SharingCountMapper sharingCountMapper;


    @Transactional
    public void addSharingModal(SharingModal modal, User user) throws BizException {
        validateParams(modal);
        log.info("add sharing modal validate success by user:{}", user.getId());

        //create modal sharing_no
        modal.setSharingNo(GeneratorUtil.generatorNo("S"));
        modal.setStatus(SharingModalStatus.NORMAL.name());
        //insert modal
        modal.setCreatedBy(String.valueOf(user.getId()));
        modal.setCreatedTime(new Date());
        sharingModalMapper.insert(modal);

        //创建统计数据记录
        SharingCount count = new SharingCount();
        count.setSharingNo(modal.getSharingNo());
        count.setTotalClick(0L);
        count.setTotalRecord(0L);
        count.setTotalUnprocess(0L);
        count.setUpdatedTime(new Date());
        sharingCountMapper.insert(count);

        log.info("create modal {} success", modal.getId());

        //add to cache
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String cacheValue = JSON.toJSONString(modal);
        operations.set(modal.getSharingNo(), cacheValue, 30, TimeUnit.DAYS);
    }

    public void updateSharingModal(SharingModal modal, User user) throws BizException {
        validateParams(modal);
        SharingModal existModal = sharingModalMapper.selectByPrimaryKey(modal.getId());
        if (existModal == null) {
            throw new BizException(ErrorCode.SHARING_MODAL_GET_FAIL);
        }
        modal.setUpdatedTime(new Date());
        modal.setUpdatedBy(String.valueOf(user.getId()));
        sharingModalMapper.updateByPrimaryKey(modal);

        //add to cache
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String cacheValue = JSON.toJSONString(modal);
        operations.set(modal.getSharingNo(), cacheValue, 30, TimeUnit.DAYS);

        //如果图像URL变动了，直接删除老的图像
        if (existModal.getHeadFileKey() != null && !existModal.getHeadFileKey().equals(modal.getHeadFileKey())) {
            AliOSSClientUtil.deleteFile(AliOSSClientUtil.getOssClient(), AliOSSClientUtil.OSS_BUCKET, existModal.getHeadFileKey());
        }
        if (existModal.getSharingFileKey() != null && !existModal.getSharingFileKey().equals(modal.getSharingFileKey())) {
            AliOSSClientUtil.deleteFile(AliOSSClientUtil.getOssClient(), AliOSSClientUtil.OSS_BUCKET, existModal.getSharingFileKey());
        }
    }

    /**
     * 管理台的尽量使用没有缓存的，这样修改保证数据库数据
     * @param sharingNo
     * @return
     */
    public SharingModal getBySharingNoNoCache(String sharingNo) {
        return sharingModalMapper.findBySharingNo(sharingNo);
    }

    public SharingModal getBySharingNo(String sharingNo) {
        if (StringUtils.isEmpty(sharingNo)) {
            return null;
        }
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String cacheValue = operations.get(sharingNo);
        if (StringUtils.isEmpty(cacheValue)) {
            SharingModal modal = sharingModalMapper.findBySharingNo(sharingNo);
            if (modal != null) {
                operations.set(modal.getSharingNo(), JSON.toJSONString(modal), 30, TimeUnit.DAYS);
            }
            return modal;
        }else {
            return JSON.parseObject(cacheValue, SharingModal.class);
        }
    }

    private void validateParams(SharingModal modal) throws BizException {
        if (modal == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(modal.getSharingTitle())) {
            log.warn("add sharing modal but sharing title is empty.");
            throw new BizException(ErrorCode.SHARING_ADD_TITLE_EMPTY);
        }
        if (StringUtils.isEmpty(modal.getSharingSubTitle())) {
            log.warn("modal subTitle can not be empty.");
            throw new BizException(ErrorCode.SHARING_ADD_SUBTITLE_EMPTY);
        }
        if (modal.getModalBodyList() != null && !modal.getModalBodyList().isEmpty()) {
            for (ModalBody body : modal.getModalBodyList()) {
                if (StringUtils.isEmpty(body.getName())) {
                    throw new BizException(ErrorCode.SHARING_ADD_MODEL_NAME_EMPTY);
                }
            }
        }
    }

    public int sharingListCount(SharingQuery query) {
        return sharingModalMapper.sharingListCount(query);
    }

    public List<SharingModal> sharingList(SharingQuery query) {
        return sharingModalMapper.sharingList(query);
    }

    public void sharingDown(Integer id, User user) throws BizException {
        if (id == null || id <= 0) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        SharingModal modal = sharingModalMapper.selectByPrimaryKey(id);
        if (modal == null) {
            log.warn("get sharing modal fail by id:{}", id);
            throw new BizException(ErrorCode.SHARING_MODAL_GET_FAIL);
        }
        log.info("update sharing modal {} status to DOWN by user:{}", id, user.getId());
        modal.setStatus(SharingModalStatus.DOWN.name());
        modal.setUpdatedBy(String.valueOf(user.getId()));
        modal.setUpdatedTime(new Date());
        sharingModalMapper.updateByPrimaryKey(modal);
        //delete redis cache;
        redisTemplate.delete(modal.getSharingNo());
    }

    public int getSharingRecordsCount(String sharingNo) {
        if (StringUtils.isEmpty(sharingNo)) {
            return 0;
        }
        return sharingRecordMapper.getSharingRecordsCount(sharingNo);
    }

    public List<SharingRecord> getSharingRecords(SharingQuery query) {
        if (query == null || StringUtils.isEmpty(query.getSharingNo())) {
            return Collections.emptyList();
        }
        return sharingRecordMapper.getSharingRecords(query);
    }

    public List<SharingProcess> getSharingProcesses(Integer recordId) {
        if (recordId == null || recordId <= 0) {
            return Collections.emptyList();
        }
        return sharingProcessMapper.getByRecordId(recordId);
    }

    @Transactional
    public List<SharingProcess> sharingProcessAdd(SharingProcess process, User user) throws BizException {
        if (process == null || process.getRecordId() == null || process.getRecordId() <= 0) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        SharingHandleStatus handleStatus = SharingHandleStatus.getByName(process.getHandleStatus());
        if (handleStatus == null) {
            throw new BizException(ErrorCode.PARAM_ERROR);
        }

        SharingRecord record = sharingRecordMapper.selectByPrimaryKey(process.getRecordId());
        if (record == null) {
            log.warn("get sharing record by id {} fail.", process.getRecordId());
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        process.setHandleStatus(handleStatus.name());
        process.setProcessTime(new Date());
        process.setProcessUser(user.getId());
        process.setProcessUserName(StringUtils.isEmpty(user.getUserName()) ? user.getPhone() : user.getUserName());
        process.setCreatedBy(String.valueOf(user.getId()));
        process.setCreatedTime(new Date());
        sharingProcessMapper.insert(process);
        log.info("record: {} add one process: {} success.", record.getId(), process.getId());
        record.setHandleStatus(handleStatus.name());
        record.setUpdatedBy(String.valueOf(user.getId()));
        record.setUpdatedTime(new Date());
        sharingRecordMapper.updateByPrimaryKey(record);
        return getSharingProcesses(process.getRecordId());
    }

    public void recordAdd(String sharingNo, SharingRecord record) throws BizException {
        if (StringUtils.isEmpty(sharingNo) || record == null
                || StringUtils.isEmpty(record.getSharingNo()) || record.getModalId() == null
                || StringUtils.isEmpty(record.getRecordTicket())) {
            log.warn("add record by sharingNo is empty.");
            throw new BizException(ErrorCode.SHARING_MODAL_ERROR);
        }
        SharingModal modal = sharingModalMapper.selectByPrimaryKey(record.getModalId());
        if (modal == null || !sharingNo.equals(modal.getSharingNo()) || !sharingNo.equals(record.getSharingNo())) {
            log.warn("get sharing fail or sharingNo is error. sharingId: {} sharingNo: {}", record.getModalId(), sharingNo);
            throw new BizException(ErrorCode.SHARING_MODAL_ERROR);
        }

        //检查是否已经提交过但是出于未处理状态，如果是，不能再次提交
        SharingRecord existRecord = sharingRecordMapper.getByRecordTicket(record.getRecordTicket());
        if (existRecord != null) {
            throw new BizException(ErrorCode.SHARING_RECORD_TICKET_EXIST);
        }

        //insert record
        record.setHandleStatus(SharingHandleStatus.UNPROCESS.name());
        record.setCreatedBy("system");
        record.setCreatedTime(new Date());

        sharingRecordMapper.insert(record);
        log.info("success save sharing: {} record: {}", sharingNo, record.getId());

        //发送邮件 提示邮件
        if (!StringUtils.isEmpty(recordEmails)) {
            List<String> toEmails = Arrays.asList(recordEmails.split(";"));
            String subject = modal.getSharingTitle() + "-分享登记";

            StringBuilder text = new StringBuilder();
            text.append("模板标题：" + modal.getSharingTitle() + "<br/>");
            text.append("模板编号:" + modal.getSharingNo() + "<br/>");
            text.append("<br/>");
            text.append("客户登记信息：<br/><br/>");
            text.append(JSON.toJSONString(record));
            text.append("<br/><br/>");
            text.append("<strong>请及时处理!</strong>");

            EmailClientUtil.sendMail(EmailClientUtil.getSession(), subject, toEmails, text.toString(), null);
        }

    }
}

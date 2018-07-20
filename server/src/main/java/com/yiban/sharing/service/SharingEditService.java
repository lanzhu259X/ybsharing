package com.yiban.sharing.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yiban.sharing.constants.SharingModalStatus;
import com.yiban.sharing.dao.SharingModalMapper;
import com.yiban.sharing.dto.SharingQuery;
import com.yiban.sharing.entities.ModalBody;
import com.yiban.sharing.entities.SharingModal;
import com.yiban.sharing.entities.User;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.exception.ErrorCode;
import com.yiban.sharing.utils.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SharingEditService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SharingModalMapper sharingModalMapper;


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

        //add to cache
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String cacheValue = JSON.toJSONString(modal);
        operations.set(modal.getSharingNo(), cacheValue, 30, TimeUnit.DAYS);
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
}

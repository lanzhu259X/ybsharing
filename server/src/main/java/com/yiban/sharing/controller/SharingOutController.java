package com.yiban.sharing.controller;

import com.alibaba.fastjson.JSON;
import com.yiban.sharing.entities.SharingModal;
import com.yiban.sharing.entities.SharingRecord;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.exception.ErrorCode;
import com.yiban.sharing.service.SharingEditService;
import com.yiban.sharing.service.SharingTongjiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/sharing/out")
public class SharingOutController {

    @Autowired
    private SharingEditService sharingEditService;
    @Autowired
    private SharingTongjiService sharingTongjiService;

    @RequestMapping(value = "/sharingmodal/{sharingNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSharingModal(@PathVariable String sharingNo) throws Exception {
        log.info("get sharing page modal by sharingNo: {}", sharingNo);
        SharingModal modal = sharingEditService.getBySharingNo(sharingNo);
        if (modal == null) {
            throw new BizException(ErrorCode.SHARING_MODAL_GET_FAIL);
        }
        sharingTongjiService.shringNoClickTimes(sharingNo); //统计打开次数
        String ticket  = UUID.randomUUID().toString();
        modal.setRecordTicket(ticket);
        return ResponseEntity.ok().body(JSON.toJSONString(modal));
    }

    @RequestMapping(value = "/{sharingNo}/submit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> recordSubmit(@PathVariable String sharingNo,
                                               @RequestBody SharingRecord record) throws Exception {
        log.info("sharing page get a submit request by sharingNo:{}", sharingNo);
        sharingEditService.recordAdd(sharingNo, record);
        return ResponseEntity.ok().build();
    }
}

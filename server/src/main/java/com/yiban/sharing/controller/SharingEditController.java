package com.yiban.sharing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiban.sharing.dto.SharingQuery;
import com.yiban.sharing.entities.*;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.exception.ErrorCode;
import com.yiban.sharing.service.SharingEditService;
import com.yiban.sharing.service.SharingTongjiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sharing/admin")
public class SharingEditController {

    @Autowired
    private SharingEditService sharingEditService;
    @Autowired
    private SharingTongjiService sharingTongjiService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSharingModal(@RequestBody SharingModal modal,
                                                  @AuthenticationPrincipal User user) throws Exception {
        log.debug("user: {} request add sharing modal.");
        sharingEditService.addSharingModal(modal, user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/modal/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSharingModal(@RequestBody SharingModal modal,
                                                     @AuthenticationPrincipal User user) throws Exception {
        log.info("user:{} request update sharing modal info.", user.getId());
        sharingEditService.updateSharingModal(modal, user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sharingList(@RequestBody SharingQuery query) throws Exception {
        int count = sharingEditService.sharingListCount(query);
        List<SharingModal> modals = new ArrayList<>();
        if (count > 0) {
            modals = sharingEditService.sharingList(query);
        }
        JSONObject result = new JSONObject();
        result.put("totalCount", count);
        result.put("data", modals);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    @RequestMapping(value = "/down/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sharingDown(@PathVariable Integer id,
                                              @AuthenticationPrincipal User user) throws Exception {
        log.debug("user:{} request down sharing by id:{}", user.getId(), id);
        sharingEditService.sharingDown(id, user);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/detail/{sharingNo}/nocache", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDetailBySharingNo(@PathVariable String sharingNo) throws Exception {
        SharingModal modal = sharingEditService.getBySharingNoNoCache(sharingNo);
        if (modal == null) {
            throw new BizException(ErrorCode.SHARING_MODAL_GET_FAIL);
        }else {
            return ResponseEntity.ok().body(JSON.toJSONString(modal));
        }
    }

    @RequestMapping(value = "/{sharingNo}/records", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSharingRecord(@RequestBody SharingQuery query,
                                                   @PathVariable String sharingNo) throws Exception {
        int count = sharingEditService.getSharingRecordsCount(sharingNo);
        List<SharingRecord> records = new ArrayList<>();
        if (count > 0) {
            query.setSharingNo(sharingNo);
            records = sharingEditService.getSharingRecords(query);
        }
        JSONObject result = new JSONObject();
        result.put("data", records);
        result.put("totalCount", count);
        return ResponseEntity.ok().body(result.toJSONString());
    }

    @RequestMapping(value = "/record/process/{recordId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSharingProcess(@PathVariable Integer recordId) throws Exception {
        List<SharingProcess> processes = sharingEditService.getSharingProcesses(recordId);
        return ResponseEntity.ok().body(JSON.toJSONString(processes));
    }

    @RequestMapping(value = "/record/process/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sharingProcessAdd(@RequestBody SharingProcess process,
                                                    @AuthenticationPrincipal User user) throws Exception {
        List<SharingProcess> processes = sharingEditService.sharingProcessAdd(process, user);
        return ResponseEntity.ok().body(JSON.toJSONString(processes));
    }

    @RequestMapping(value = "/count/{sharingNo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sharingModalCount(@PathVariable String sharingNo) {
        SharingCount sharingCount = sharingTongjiService.getSharingCount(sharingNo);
        return ResponseEntity.ok().body(JSON.toJSONString(sharingCount));
    }

}

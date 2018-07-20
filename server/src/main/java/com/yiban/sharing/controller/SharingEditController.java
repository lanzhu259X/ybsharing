package com.yiban.sharing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiban.sharing.dto.SharingQuery;
import com.yiban.sharing.entities.SharingModal;
import com.yiban.sharing.entities.User;
import com.yiban.sharing.service.SharingEditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sharing/admin")
public class SharingEditController {

    @Autowired
    private SharingEditService sharingEditService;

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addSharingModal(@RequestBody SharingModal modal,
                                                  @AuthenticationPrincipal User user) throws Exception {
        log.debug("user: {} request add sharing modal.");
        sharingEditService.addSharingModal(modal, user);
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
}

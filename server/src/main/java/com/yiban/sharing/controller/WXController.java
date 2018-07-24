package com.yiban.sharing.controller;

import com.alibaba.fastjson.JSON;
import com.yiban.sharing.entities.WXSignEntity;
import com.yiban.sharing.service.WXService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/wecat")
public class WXController {

    @Autowired
    private WXService wxService;

    @RequestMapping(value = "/config", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getWXSharingConfig(@RequestParam("sharingUrl") String sharingUrl) throws Exception {
        WXSignEntity entity = wxService.getWXSignConfig(sharingUrl);
        log.info("wx sharing config: {}", JSON.toJSONString(entity));
        return ResponseEntity.ok().body(JSON.toJSONString(entity));
    }
}

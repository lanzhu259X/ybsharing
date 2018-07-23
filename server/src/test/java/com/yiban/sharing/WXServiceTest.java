package com.yiban.sharing;

import com.alibaba.fastjson.JSON;
import com.yiban.sharing.entities.WXSignEntity;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.service.WXService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class WXServiceTest {

    @Autowired
    private WXService wxService;

    @Test
    public void getWXAccessTokenTest() {
        String result = wxService.getWxAccessToken();
        log.info("weixin access-token:{}", result);
    }

    @Test
    public void getWXSignConfig() {
        try {
            WXSignEntity entity = wxService.getWXSignConfig("http://sharing.yibanjf.com/dist/");
            log.info(JSON.toJSONString(entity));
        } catch (BizException e) {
            log.error("BizException ", e);
        }
    }

}

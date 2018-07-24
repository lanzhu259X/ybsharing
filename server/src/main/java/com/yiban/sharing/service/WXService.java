package com.yiban.sharing.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yiban.sharing.entities.WXSignEntity;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.exception.ErrorCode;
import com.yiban.sharing.utils.WXSignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 提供一些微信的接口服务
 */
@Slf4j
@Service
public class WXService {

    private static final String WX_ACCESS_TOKEN_CACHE_KEY = "YB_WX_ACCESS_TOKEN";
    private static final String WX_JSAPI_TICKET_CACHE_KEY = "YB_WX_JSAPI_TICKET";



    @Value("${wx.app.id}")
    private String wxAppId;
    @Value("${wx.app.secret}")
    private String wxAppSecret;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RestTemplate restTemplate;

    private Lock lock = new ReentrantLock();

    public String getWxAccessToken() {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String cache = operations.get(WX_ACCESS_TOKEN_CACHE_KEY);
        if (StringUtils.isNotEmpty(cache)) {
            return cache;
        }

        //多线程拿时防止下多个请求同时去请求微信
        lock.lock();
        try {
            String wxAccessToken = getWxAccessTokenByWX();
            if (wxAccessToken != null) {
                //加入缓存 微信的token过期时间7200秒
                operations.set(WX_ACCESS_TOKEN_CACHE_KEY, wxAccessToken, 7000, TimeUnit.SECONDS);
            }
            return wxAccessToken;
        }finally {
            lock.unlock();
        }
    }

    private String getWxAccessTokenByWX() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxAppId + "&secret=" + wxAppSecret;
        try{
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();
                log.info("get weixin access_token: {}", body);
                JSONObject json = JSON.parseObject(body);
                String result = (String) json.get("access_token");
                return result;
            }
        }catch (Exception e) {
            log.error("get weixin access_token fail.", e);
        }
        log.warn("get weixin access_token result is fail.");
        return null;
    }

    public String getWXJsapiTicket(String accessToken) {
        if (StringUtils.isEmpty(accessToken)) {
            return null;
        }
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String cache = operations.get(WX_JSAPI_TICKET_CACHE_KEY);
        if (StringUtils.isNotEmpty(cache)) {
            return cache;
        }

        //多线程拿时防止下多个请求同时去请求微信
        lock.lock();
        try {
            String ticket = getWxjsapiTicketByWx(accessToken);
            if (ticket != null) {
                //加入缓存 微信的token过期时间7200秒
                operations.set(WX_JSAPI_TICKET_CACHE_KEY, ticket, 7000, TimeUnit.SECONDS);
            }
            return ticket;
        }finally {
            lock.unlock();
        }
    }

    private String getWxjsapiTicketByWx(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ accessToken +"&type=jsapi";
        try{
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();
                log.info("get weixin jsapi_ticket: {}", body);
                JSONObject json = JSON.parseObject(body);
                String result = (String) json.get("ticket");
                return result;
            }
        }catch (Exception e) {
            log.error("get weixin jsapi_ticket fail.", e);
        }
        log.warn("get weixin jsapi_ticket result is fail.");
        return null;
    }

    public WXSignEntity getWXSignConfig(String url) throws BizException {
        String token = getWxAccessToken();
        String ticket = getWXJsapiTicket(token);
        if (StringUtils.isEmpty(ticket)) {
            log.warn("get weixin jsapi_ticket fail.");
            throw new BizException(ErrorCode.WX_JSAPI_TICKET_FAIL);
        }

        WXSignEntity entity = WXSignUtil.getWXSign(ticket, url);
        if (entity != null) {
            entity.setAppId(wxAppId);
            return entity;
        }else {
            throw new BizException(ErrorCode.WX_SIGN_CONFIG_FAIL);
        }

    }


}

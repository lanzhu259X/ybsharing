package com.yiban.sharing.utils;

import com.yiban.sharing.entities.WXSignEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信签名
 */
@Slf4j
public class WXSignUtil {

    /**
     * 微信分享url签名
     * @param jsapiTicket 通过微信access_token获取的jsapi_ticket
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return
     */
    public static WXSignEntity getWXSign(String jsapiTicket, String url) {
        String nonce_str = create_nonce_str(); //随机码
        String timestamp = create_timestamp(); //签名时间 秒

        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        String string1 ="jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1"); //需要使用SHA1加密
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException ", e);
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException ", e);
        }

        WXSignEntity entity = new WXSignEntity();
        entity.setJsapi_ticket(jsapiTicket);
        entity.setNonceStr(nonce_str);
        entity.setSignature(signature);
        entity.setTimestamp(timestamp);
        entity.setUrl(url);
        return entity;
    }

    //生成签名
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}

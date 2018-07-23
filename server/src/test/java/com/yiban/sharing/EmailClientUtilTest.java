package com.yiban.sharing;

import com.yiban.sharing.utils.EmailClientUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailClientUtilTest {

    @Test
    public void sendEmailTest() {
        List<String> toList = Arrays.asList("liliang@yibanjf.com", "liliang259X@163.com", "shanxuekai@yibanjf.com");
        String subject = "测试邮件-" + RandomUtils.nextInt(100, 999);
        String text = "测试邮件<br/>医伴ERP：http://erp.yibanjf.com/page <br/> 医伴分享：http://sharing.yibanjf.com/dist <br/> 验证码: <strong>" + RandomStringUtils.randomNumeric(6) +"</strong> <br/>";
        List<File> files = new ArrayList<>();

        File file = new File("C:" + File.separator + "Users"+ File.separator +"Public"+ File.separator +"Pictures"+ File.separator +"Sample Pictures"+ File.separator +"app_code.b6c966.jpg");
        files.add(file);

        EmailClientUtil.sendMail(EmailClientUtil.getSession(), subject, toList, text, files);
    }
}

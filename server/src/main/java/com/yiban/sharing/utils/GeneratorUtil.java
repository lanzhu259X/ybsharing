package com.yiban.sharing.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratorUtil {

    public static String generatorNo(String type) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmm");
        StringBuilder sb = new StringBuilder();
        sb.append(type == null ? "" : type);
        String timeStr = formatter.format(new Date());
        sb.append(timeStr);
        sb.append("-");
        sb.append(RandomStringUtils.randomNumeric(4));
        return sb.toString();
    }
}

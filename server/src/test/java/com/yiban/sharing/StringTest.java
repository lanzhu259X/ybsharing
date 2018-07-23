package com.yiban.sharing;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class StringTest {

    @Test
    public void testRandString() {
        System.out.println(RandomStringUtils.random(8));
        System.out.println(RandomStringUtils.random(8, "UTF-8"));
        System.out.println(RandomStringUtils.randomAlphabetic(15));
        System.out.println(RandomStringUtils.randomAlphanumeric(15));
    }
}

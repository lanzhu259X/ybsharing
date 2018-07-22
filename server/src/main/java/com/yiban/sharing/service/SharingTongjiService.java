package com.yiban.sharing.service;

import com.yiban.sharing.dao.SharingCountMapper;
import com.yiban.sharing.entities.SharingCount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class SharingTongjiService {

    private static final String COUNT_KEY = "COUNT.";

    //建立一个线程池，每次请求过来使用一个线程去统计
    private ExecutorService executor;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SharingCountMapper sharingCountMapper;

    @PostConstruct
    public void initThreadPool() {
        this.executor = Executors.newFixedThreadPool(10);
    }

    public void shringNoClickTimes(final String sharingNo) {
        executor.execute(() -> {
            redisTemplate.opsForValue().increment( COUNT_KEY + sharingNo, 1L);
        });
    }

    /**
     * 不考虑数据是否存在，应该在创建模板的时候创建出数据。
     * @param sharingNo
     * @return
     */
    public SharingCount getSharingCount(String sharingNo) {
        //获取之前，先统计一次
        String totalClickStr = redisTemplate.opsForValue().get(COUNT_KEY + sharingNo);
        long totalClick = StringUtils.isEmpty(totalClickStr) ? 0L : Long.valueOf(totalClickStr);
        sharingCountMapper.updateBySharingNo(sharingNo, totalClick);
        return sharingCountMapper.getBySharingNo(sharingNo);
    }
}

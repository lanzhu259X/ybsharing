package com.yiban.sharing;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.yiban.sharing.utils.AliOSSClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class AliOSSClientUtilTest {


    @Test
    public void getBucket() {
        OSSClient ossClient = AliOSSClientUtil.getOssClient();
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(JSON.toJSONString(bucket));
        }
        ossClient.shutdown();
    }

    @Test
    public void createBucket() {
        OSSClient ossClient = AliOSSClientUtil.getOssClient();
//        String name = AliOSSClientUtil.createBucketName(ossClient, AliOSSClientUtil.OSS_BUCKET);

        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(JSON.toJSONString(bucket));
        }

        ossClient.shutdown();
    }

}

package com.yiban.sharing.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AliOSSClientUtil {

    public static final String OSS_BUCKET = "yibansharing";
    private static final String FOLDER = "image/";

    private static final String OSS_END_POINT = "https://oss-cn-shanghai.aliyuncs.com";
    private static final String OSS_ACCESS_KEY = "LTAIULFYwI6R0YNJ";
    private static final String OSS_ACCESS_SECRET = "MjpDvT0AUSG7JuvoW1H7YqgAP3TitO";

    private static OSSClient ossClient;
    public static OSSClient getOssClient() {
        if (ossClient == null) {
            ossClient = new OSSClient(OSS_END_POINT, OSS_ACCESS_KEY, OSS_ACCESS_SECRET);
        }
        return ossClient;
    }

    /**
     * 创建存储空间
     * @param ossClient      OSS连接
     * @param bucketName 存储空间
     * @return
     */
    public  static String createBucketName(OSSClient ossClient,String bucketName){
        //存储空间
        final String bucketNames=bucketName;
        if(!ossClient.doesBucketExist(bucketName)){
            //创建存储空间
            Bucket bucket=ossClient.createBucket(bucketName);
            log.info("create bucket {} success.", bucketName);
            return bucket.getName();
        }
        return bucketNames;
    }

    /**
     * 删除存储空间buckName
     * @param ossClient  oss对象
     * @param bucketName  存储空间
     */
    public static  void deleteBucket(OSSClient ossClient, String bucketName){
        ossClient.deleteBucket(bucketName);
        log.info("remove {} Bucket success.", bucketName);
    }

    /**
     * 创建模拟文件夹
     * @param ossClient oss连接
     * @param bucketName 存储空间
     * @param folder   模拟文件夹名如"sharing/xxx/"
     * @return  文件夹名
     */
    public  static String createFolder(OSSClient ossClient,String bucketName,String folder){
        //文件夹名
        final String keySuffixWithSlash = FOLDER + folder;
        //判断文件夹是否存在，不存在则创建
        if(!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)){
            //创建文件夹
            ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
            log.info("bucket {} created folder {} success.", bucketName, FOLDER + folder);
            //得到文件夹名
            OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
            String fileDir=object.getKey();
            return fileDir;
        }
        return keySuffixWithSlash;
    }

    /**
     * 根据key删除OSS服务器上的文件
     * @param ossClient  oss连接
     * @param bucketName  存储空间
     * @param key Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
     */
    public static void deleteFile(OSSClient ossClient, String bucketName, String key){
        ossClient.deleteObject(bucketName, FOLDER + key);
        log.info("remove bucket {} file {} success.", bucketName, FOLDER + key);
    }

    /**
     * 上传图片至OSS
     * @param ossClient  oss连接
     * @param file 上传文件（文件全路径如：D:\\image\\cake.jpg）
     * @param bucketName  存储空间
     * @param key key
     * @param extra 自定义的元数据信息
     * @return String 返回的唯一MD5数字签名
     * */
    public static String uploadObject2OSS(OSSClient ossClient, MultipartFile file, String bucketName, String key, String extra) {
        String resultStr = null;
        try {

            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(file.getSize());
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            metadata.addUserMetadata("X-OSS-USER", String.valueOf(extra)); //自定义的用户元信息
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, FOLDER + key,
                    new ByteArrayInputStream(file.getBytes()), metadata);
            log.info("upload file to aliyun oss bucketName {} by key: {} success", bucketName, FOLDER + key);

            //获取访问的url
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10); //有效期设置为10年
            // 生成URL
            URL url = ossClient.generatePresignedUrl(bucketName, FOLDER + key, expiration);
            if (url != null) {
                return url.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("upload file to aliyun oss exception {}.", e.getMessage(), e);
        }
        return resultStr;
    }







}

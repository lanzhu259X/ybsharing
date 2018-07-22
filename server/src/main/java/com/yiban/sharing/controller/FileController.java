package com.yiban.sharing.controller;

import com.alibaba.fastjson.JSONObject;
import com.yiban.sharing.constants.FileUploadType;
import com.yiban.sharing.exception.BizException;
import com.yiban.sharing.exception.ErrorCode;
import com.yiban.sharing.utils.AliOSSClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest mtRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = mtRequest.getFile("file");
        //以输入流的形式上传文件
        String genName = file.getOriginalFilename();
        if (!FileUploadType.validate(genName)) {
            throw new BizException(ErrorCode.UPLOAD_FILE_TYPE_ERROR);
        }
        //截取最后的
        String end  = "";
        if (genName.lastIndexOf('.') > 0) {
            end = genName.substring(genName.lastIndexOf('.'));
        }
        String keyPre = mtRequest.getParameter("keyPre");
        if (StringUtils.isEmpty(keyPre)) {
            keyPre = "default-";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
        String key = keyPre + dateFormat.format(new Date()) + end;
        if (file == null || file.isEmpty()) {
            log.warn("request upload file but file is empty.");
            throw new BizException(ErrorCode.PARAM_ERROR);
        }
        String url = AliOSSClientUtil.uploadObject2OSS(AliOSSClientUtil.getOssClient(), file, AliOSSClientUtil.OSS_BUCKET,
                key, keyPre);
        log.info("upload file success, file url:{}", url);
        JSONObject result = new JSONObject();
        result.put("url", url);
        result.put("key", key); //上传文件的key
        return ResponseEntity.ok().body(result.toJSONString());
    }

    @RequestMapping(value = "/remove/{key}", method = RequestMethod.DELETE)
    public ResponseEntity<String> removeFile(@PathVariable String key) throws Exception {
        log.info("request to remove file by key: {}", key);
        AliOSSClientUtil.deleteFile(AliOSSClientUtil.getOssClient(), AliOSSClientUtil.OSS_BUCKET, key);
        return ResponseEntity.ok().build();
    }
}

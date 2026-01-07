package com.wzz.Util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Date 2020/10/26 10:25
 * @created by wzz
 */
//阿里云上传的工具类
@Component
public class OSSUtil {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.key-prefix}")
    private String keyPrefix;

    private static String ENDPOINT;
    private static String ACCESSKEYID;
    private static String ACCESSKEYSECRET;
    private static String BUCKETNAME;
    private static String KEY;

    @PostConstruct
    public void init() {
        ENDPOINT = this.endpoint;
        ACCESSKEYID = this.accessKeyId;
        ACCESSKEYSECRET = this.accessKeySecret;
        BUCKETNAME = this.bucketName;
        KEY = this.keyPrefix;
    }

    public static String picOSS(MultipartFile uploadFile) throws Exception {
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        // 上传
        long time = new Date().getTime();
        String date = sf.format(time);

        //上传的文件名
        String fileName = UUID.randomUUID().toString().substring(0, 5) + uploadFile.getOriginalFilename();

        //设置请求头
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
        //上传开始
        ossClient.putObject(BUCKETNAME, KEY + date + "/" + fileName, new ByteArrayInputStream(uploadFile.getBytes()),objectMetadata);

        // 生成签名URL (有效期10年，适用于私有Bucket)
        Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl(BUCKETNAME, KEY + date + "/" + fileName, expiration).toString();

        // 关闭client
        ossClient.shutdown();
        return url;
    }

    //根据文件的类型 设置请求头
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }
}

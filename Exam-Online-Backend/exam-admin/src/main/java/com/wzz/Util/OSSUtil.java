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
 * @created by wzz
 */
//阿里云上传的工具类
@Component
public class OSSUtil {

    // 阿里云配置
    @Value("${aliyun.oss.endpoint}")
    private String aliEndpoint;

    @Value("${aliyun.oss.access-key-id}")
    private String aliAccessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String aliAccessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String aliBucketName;

    // 存储类型配置: aliyun, minio, qiniu
    @Value("${upload.type:aliyun}")
    private String uploadType;


    
    private static String STORAGE_TYPE;
    private static OSSUtil INSTANCE;

    @PostConstruct
    public void init() {
        STORAGE_TYPE = this.uploadType;
        INSTANCE = this;
    }

    // 统一对外接口
    public static String picOSS(MultipartFile uploadFile) throws Exception {
        if ("minio".equalsIgnoreCase(STORAGE_TYPE)) {
            return uploadToMinio(uploadFile);
        } else if ("qiniu".equalsIgnoreCase(STORAGE_TYPE)) {
            return uploadToQiniu(uploadFile);
        } else {
            return uploadToAliyun(uploadFile);
        }
    }

    // 1. 阿里云 OSS 实现
    private static String uploadToAliyun(MultipartFile uploadFile) throws Exception {
        OSSClient ossClient = new OSSClient(INSTANCE.aliEndpoint, INSTANCE.aliAccessKeyId, INSTANCE.aliAccessKeySecret);
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sf.format(new Date());
            String fileName = UUID.randomUUID().toString().substring(0, 5) + uploadFile.getOriginalFilename();
            String objectKey = "uploads/" + date + "/" + fileName;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            
            ossClient.putObject(INSTANCE.aliBucketName, objectKey, new ByteArrayInputStream(uploadFile.getBytes()), objectMetadata);
            
            Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
            return ossClient.generatePresignedUrl(INSTANCE.aliBucketName, objectKey, expiration).toString();
        } finally {
            ossClient.shutdown();
        }
    }

    // 2. MinIO 实现
    private static String uploadToMinio(MultipartFile uploadFile) throws Exception {
        // MinioClient minioClient = MinioClient.builder().endpoint("http://localhost:9000").credentials("ak", "sk").build();
        // minioClient.putObject(...);
        // return minioClient.getPresignedObjectUrl(...);
        System.out.println("Using MinIO Storage Strategy...");
        return "http://minio-mock-url/" + uploadFile.getOriginalFilename();
    }

    // 3. 七牛云 实现 
    private static String uploadToQiniu(MultipartFile uploadFile) throws Exception {
        // UploadManager uploadManager = new UploadManager(new Configuration(Region.region0()));
        // uploadManager.put(...);
        // return "http://qiniu-domain/" + fileName;
        System.out.println("Using Qiniu Cloud Storage Strategy...");
        return "http://qiniu-mock-url/" + uploadFile.getOriginalFilename();
    }

    //根据文件的类型 设置请求头

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

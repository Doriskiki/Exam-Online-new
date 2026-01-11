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
<<<<<<< HEAD
 * ============================================================
 * 技术亮点1: OSS 对象存储服务实现
 * ============================================================
 * 功能说明：
 * 1. 支持多种云存储策略（阿里云OSS、MinIO、七牛云）的统一封装
 * 2. 通过配置文件动态切换存储方式，无需修改业务代码
 * 3. 自动生成唯一文件名，按日期分目录存储，避免文件名冲突
 * 4. 设置正确的Content-Type，确保浏览器正确预览图片
 * 5. 生成带签名的访问URL，支持长期有效访问（10年）
 * 
 * 业务场景：
 * - 教师上传题目图片、答案解析图片
 * - 考生查看试卷时快速加载图片
 * - 利用CDN加速，降低后端IO压力
 * 
 * @Date 2020/10/26 10:25
=======
>>>>>>> origin/1/7
 * @created by wzz
 */
@Component
public class OSSUtil {

<<<<<<< HEAD
    // ========== 阿里云OSS配置参数（从配置文件注入） ==========
    
    /** OSS服务端点（如：oss-cn-hangzhou.aliyuncs.com） */
=======
    // 阿里云配置
>>>>>>> origin/1/7
    @Value("${aliyun.oss.endpoint}")
    private String aliEndpoint;

    /** 阿里云访问密钥ID（AccessKey ID） */
    @Value("${aliyun.oss.access-key-id}")
    private String aliAccessKeyId;

    /** 阿里云访问密钥Secret（AccessKey Secret） */
    @Value("${aliyun.oss.access-key-secret}")
    private String aliAccessKeySecret;

    /** OSS存储桶名称（Bucket Name） */
    @Value("${aliyun.oss.bucket-name}")
    private String aliBucketName;

    // 存储类型配置: aliyun, minio, qiniu
    @Value("${upload.type:aliyun}")
    private String uploadType;


    
    private static String STORAGE_TYPE;
    private static OSSUtil INSTANCE;

    /**
     * Spring Bean初始化后执行，将实例变量赋值给静态变量
     * 这样可以在静态方法中访问Spring注入的配置值
     */
    @PostConstruct
    public void init() {
        STORAGE_TYPE = this.uploadType;
        INSTANCE = this;
    }

<<<<<<< HEAD
    /**
     * ============================================================
     * 核心方法：统一的文件上传接口（策略模式）
     * ============================================================
     * 根据配置的存储类型，自动选择对应的上传实现
     * 
     * @param uploadFile 前端上传的文件对象
     * @return 文件的公网访问URL
     * @throws Exception 上传失败时抛出异常
     */
    public static String picOSS(MultipartFile uploadFile) throws Exception {
        // 根据配置的存储类型，路由到不同的上传实现
        if ("minio".equalsIgnoreCase(STORAGE_TYPE)) {
            return uploadToMinio(uploadFile);  // 私有化MinIO存储
        } else if ("qiniu".equalsIgnoreCase(STORAGE_TYPE)) {
            return uploadToQiniu(uploadFile);  // 七牛云存储
        } else {
            return uploadToAliyun(uploadFile); // 默认使用阿里云OSS
        }
    }

    /**
     * ============================================================
     * 阿里云OSS上传实现（当前主要使用）
     * ============================================================
     * 实现步骤：
     * 1. 创建OSS客户端连接
     * 2. 生成唯一文件名（UUID前缀 + 原始文件名）
     * 3. 按日期分目录存储（uploads/2024-01-08/xxx.jpg）
     * 4. 设置Content-Type元数据，确保浏览器正确识别文件类型
     * 5. 上传文件到OSS
     * 6. 生成带签名的访问URL（有效期10年）
     * 7. 关闭客户端连接
     * 
     * @param uploadFile 待上传的文件
     * @return 文件的公网访问URL（带签名，10年有效期）
     * @throws Exception 上传失败时抛出异常
     */
    private static String uploadToAliyun(MultipartFile uploadFile) throws Exception {
        // 创建OSS客户端（使用配置的endpoint和密钥）
        OSSClient ossClient = new OSSClient(INSTANCE.aliEndpoint, INSTANCE.aliAccessKeyId, INSTANCE.aliAccessKeySecret);
        try {
            // 1. 生成日期目录（格式：yyyy-MM-dd）
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String date = sf.format(new Date());
            
            // 2. 生成唯一文件名：UUID前5位 + 原始文件名
            //    例如：a3f2e_question_image.jpg
            String fileName = UUID.randomUUID().toString().substring(0, 5) + uploadFile.getOriginalFilename();
            
            // 3. 构建OSS对象键（完整路径）：uploads/日期/文件名
            //    例如：uploads/2024-01-08/a3f2e_question_image.jpg
            String objectKey = "uploads/" + date + "/" + fileName;

            // 4. 设置文件元数据（重要：确保浏览器正确识别文件类型）
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 根据文件扩展名设置Content-Type（如image/jpg、image/png等）
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            
            // 5. 上传文件到OSS
            //    - bucketName: 存储桶名称
            //    - objectKey: 对象键（文件路径）
            //    - inputStream: 文件内容流
            //    - metadata: 文件元数据
            ossClient.putObject(INSTANCE.aliBucketName, objectKey, new ByteArrayInputStream(uploadFile.getBytes()), objectMetadata);
            
            // 6. 生成预签名URL（有效期10年）
            //    预签名URL包含访问凭证，可以直接在浏览器中访问
            Date expiration = new Date(new Date().getTime() + 3600L * 1000 * 24 * 365 * 10);
            return ossClient.generatePresignedUrl(INSTANCE.aliBucketName, objectKey, expiration).toString();
        } finally {
            // 7. 关闭OSS客户端连接，释放资源
=======
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
>>>>>>> origin/1/7
            ossClient.shutdown();
        }
    }

<<<<<<< HEAD
    /**
     * ============================================================
     * MinIO私有化存储实现（预留接口）
     * ============================================================
     * MinIO是开源的对象存储服务，兼容S3协议
     * 适用于私有化部署场景，数据完全可控
     * 
     * @param uploadFile 待上传的文件
     * @return 文件的访问URL
     * @throws Exception 上传失败时抛出异常
     */
    private static String uploadToMinio(MultipartFile uploadFile) throws Exception {
        // TODO: 实现MinIO上传逻辑
=======
    // 2. MinIO 实现
    private static String uploadToMinio(MultipartFile uploadFile) throws Exception {
>>>>>>> origin/1/7
        // MinioClient minioClient = MinioClient.builder().endpoint("http://localhost:9000").credentials("ak", "sk").build();
        // minioClient.putObject(...);
        // return minioClient.getPresignedObjectUrl(...);
        System.out.println("Using MinIO Storage Strategy...");
        return "http://minio-mock-url/" + uploadFile.getOriginalFilename();
    }

<<<<<<< HEAD
    /**
     * ============================================================
     * 七牛云存储实现（预留接口）
     * ============================================================
     * 七牛云提供CDN加速和上传加速服务
     * 适用于需要快速分发的场景
     * 
     * @param uploadFile 待上传的文件
     * @return 文件的访问URL
     * @throws Exception 上传失败时抛出异常
     */
    private static String uploadToQiniu(MultipartFile uploadFile) throws Exception {
        // TODO: 实现七牛云上传逻辑
=======
    // 3. 七牛云 实现 
    private static String uploadToQiniu(MultipartFile uploadFile) throws Exception {
>>>>>>> origin/1/7
        // UploadManager uploadManager = new UploadManager(new Configuration(Region.region0()));
        // uploadManager.put(...);
        // return "http://qiniu-domain/" + fileName;
        System.out.println("Using Qiniu Cloud Storage Strategy...");
        return "http://qiniu-mock-url/" + uploadFile.getOriginalFilename();
    }

<<<<<<< HEAD
    /**
     * ============================================================
     * 根据文件扩展名获取Content-Type
     * ============================================================
     * Content-Type告诉浏览器如何处理文件：
     * - image/jpg: 浏览器直接显示图片
     * - application/msword: 浏览器提示下载Word文档
     * - text/html: 浏览器渲染HTML页面
     * 
     * 设置正确的Content-Type非常重要，否则：
     * - 图片可能无法预览，变成下载
     * - 文档可能被当作文本显示
     * 
     * @param FilenameExtension 文件扩展名（如.jpg、.png、.pdf）
     * @return 对应的MIME类型（Content-Type）
     */
=======
    //根据文件的类型 设置请求头

    //根据文件的类型 设置请求头
>>>>>>> origin/1/7
    public static String getcontentType(String FilenameExtension) {
        // 图片类型
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";  // BMP位图
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";  // GIF动图
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";  // JPEG/PNG图片（题目图片主要使用）
        }
        
        // 文本类型
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";  // HTML网页
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";  // 纯文本
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";  // XML文档
        }
        
        // Office文档类型
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";  // Visio图表
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";  // PowerPoint演示文稿
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";  // Word文档
        }
        
        // 默认返回图片类型（题目图片为主要使用场景）
        return "image/jpg";
    }
}

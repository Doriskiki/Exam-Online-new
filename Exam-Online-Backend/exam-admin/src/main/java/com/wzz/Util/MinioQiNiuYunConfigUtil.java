package com.wzz.Util;

/**
 * MinIO 与七牛云的配置片段生成工具（不包含密钥）。
 */
public final class MinioQiNiuYunConfigUtil {

    private MinioQiNiuYunConfigUtil() {
        // utility class
    }

    /**
     * MinIO 配置片段，按需填入 access-key/secret-key。
     */
    public static String minioYaml() {
        return "minio:\n" +
                "  endpoint: http://localhost:9000\n" +
                "  access-key: \n" +
                "  secret-key: \n" +
                "  bucket: exam-bucket\n" +
                "  region: us-east-1\n" +
                "  secure: false\n" +
                "  object-prefix: uploads/\n";
    }

    /**
     * 七牛云配置片段，按需填入 access-key/secret-key。
     */
    public static String qiniuYaml() {
        return "qiniu:\n" +
                "  access-key: \n" +
                "  secret-key: \n" +
                "  bucket: exam-bucket\n" +
                "  domain: https://cdn.your-domain.com\n" +
                "  region: z0  # 华东:z0 华北:z1 华南:z2 北美:na0 新加坡:as0\n" +
                "  object-prefix: uploads/\n";
    }
}

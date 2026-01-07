package com.wzz.Util;

import com.wzz.entity.Answer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * ============================================================
 * 技术亮点5: 加盐SHA-256加密工具类
 * ============================================================
 * 功能说明：
 * 1. 使用SHA-256算法对密码进行哈希加密
 * 2. 加盐（Salt）机制：防止彩虹表攻击
 * 3. 单向加密：无法从哈希值反推原始密码
 * 4. 提供辅助方法：在答案列表中查找题目索引
 * 
 * 加密原理：
 * - SHA-256：安全哈希算法，输出256位（64个十六进制字符）
 * - 加盐：在密码后拼接随机字符串（盐值），增加破解难度
 * - 单向性：即使两个用户密码相同，加盐后的哈希值也不同
 * 
 * 安全优势（相比MD5）：
 * - 抗碰撞性更强：SHA-256比MD5更难找到碰撞
 * - 输出更长：256位 vs 128位，暴力破解难度更大
 * - 未被攻破：MD5已被证明存在碰撞漏洞
 * 
 * 使用场景：
 * - 用户注册：密码 + 盐值 -> SHA-256 -> 存入数据库
 * - 用户登录：输入密码 + 盐值 -> SHA-256 -> 对比数据库哈希值
 * - 密码验证：不存储明文密码，只存储哈希值
 * 
 * 进一步优化建议：
 * - 使用PBKDF2/BCrypt/Argon2等慢哈希算法
 * - 增加迭代次数，提高计算成本
 * - 使用更长的盐值（建议16字节以上）
 */
public class SaltEncryption {

    /**
     * ============================================================
     * 加盐SHA-256加密
     * ============================================================
     * 加密流程：
     * 1. 将密码和盐值拼接：password + salt
     * 2. 使用SHA-256算法计算哈希值
     * 3. 将字节数组转换为十六进制字符串
     * 4. 返回64位十六进制字符串
     * 
     * 盐值（Salt）的作用：
     * - 防止彩虹表攻击：预先计算的哈希表无法使用
     * - 相同密码不同哈希：即使两个用户密码相同，盐值不同导致哈希值不同
     * - 增加破解难度：攻击者需要为每个用户单独破解
     * 
     * 示例：
     * 密码：123456
     * 盐值：abc123
     * 拼接：123456abc123
     * SHA-256：e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855
     * 
     * 注意事项：
     * - 盐值应该随机生成，每个用户不同
     * - 盐值需要和哈希值一起存储在数据库中
     * - 验证密码时，使用相同的盐值进行哈希计算
     * 
     * @param password 原始密码（明文）
     * @param salt 盐值（随机字符串，建议16字节以上）
     * @return SHA-256哈希值（64位十六进制字符串）
     * @throws NoSuchAlgorithmException 如果系统不支持SHA-256算法
     */
    public static String saltEncryption(String password, String salt) throws NoSuchAlgorithmException {
        // 1. 获取SHA-256消息摘要实例
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        
        // 2. 将密码和盐值拼接，转换为字节数组，计算哈希值
        //    使用UTF-8编码确保中文等字符正确处理
        byte[] hashed = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
        
        // 3. 将字节数组转换为十六进制字符串
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) {
            // 将每个字节转换为2位十六进制数
            String hex = Integer.toHexString(0xff & b);
            // 如果只有1位，前面补0（例如：0x0a -> "0a"）
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        
        // 4. 返回64位十六进制字符串
        return sb.toString();
    }

    /**
     * ============================================================
     * 辅助方法：根据题目ID获取答案在列表中的索引
     * ============================================================
     * 功能说明：
     * 在答案列表中查找指定题目ID的答案，返回其索引位置
     * 
     * 使用场景：
     * - 考试评分：根据题目ID查找标准答案
     * - 答案对比：将考生答案与标准答案进行比对
     * 
     * 时间复杂度：O(n)，n为答案列表长度
     * 
     * 优化建议：
     * - 如果频繁查询，可以使用HashMap缓存（题目ID -> 答案对象）
     * - 时间复杂度可优化到O(1)
     * 
     * @param list 答案列表
     * @param questionId 题目ID
     * @return 答案在列表中的索引，未找到返回-1
     */
    public static int getIndex(List<Answer> list, Integer questionId) {
        // 遍历答案列表
        for (int i = 0; i < list.size(); i++) {
            // 找到匹配的题目ID
            if (list.get(i).getQuestionId() == questionId) {
                return i;  // 返回索引
            }
        }
        // 未找到，返回-1
        return -1;
    }
}

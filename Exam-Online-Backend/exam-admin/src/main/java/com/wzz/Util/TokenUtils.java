package com.wzz.Util;

/**
 * @created by wzz
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.wzz.vo.TokenVo;

public class TokenUtils {
    
    // ========== JWT配置参数 ==========
    
    /** Token过期时间：5小时（单位：毫秒） */
    private static final long EXPIRE_TIME = 60 * 60 * 1000 * 5;
    
    /** 
     * JWT签名密钥（对称加密）
     * 注意：生产环境应该：
     * 1. 从配置文件或环境变量读取
     * 2. 使用更复杂的密钥（建议32位以上随机字符串）
     * 3. 定期轮换密钥
     * 4. 使用密钥管理服务（KMS）存储
     */
    private static final String TOKEN_SECRET = "wangzhouzhou";

    /**
     * ============================================================
     * 生成JWT Token
     * ============================================================
     * 工作流程：
     * 1. 设置过期时间（当前时间 + 5小时）
     * 2. 选择签名算法（HMAC-SHA256）
     * 3. 设置JWT头部信息（类型、算法）
     * 4. 设置载荷（Payload）：用户信息
     * 5. 使用密钥签名，生成Token字符串
     * 
     * Token结构（三部分，用.分隔）：
     * - Header（头部）：{"Type":"Jwt","alg":"HS256"}
     * - Payload（载荷）：{"roleId":"1","username":"zhangsan",...}
     * - Signature（签名）：HMAC-SHA256(header + payload, secret)
     * 
     * 示例Token：
     * eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlSWQiOiIxIiwidXNlcm5hbWUiOiJ6aGFuZ3NhbiJ9.xxx
     * 
     * @param token 用户信息对象（包含角色ID、用户名、密码、头像、真实姓名）
     * @return JWT Token字符串，失败返回null
     */
    public static String createToken(TokenVo token) {
        try {
            // 1. 计算过期时间：当前时间 + 5小时
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            
            // 2. 创建签名算法：HMAC-SHA256 + 密钥
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            
            // 3. 设置JWT头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");      // Token类型
            header.put("alg", "HS256");     // 签名算法
            
            // 4. 构建JWT并签名
            return JWT.create()
                    .withHeader(header)                          // 设置头部
                    .withClaim("roleId", token.getRoleId())      // 载荷：角色ID（用于权限控制）
                    .withClaim("username", token.getUsername())  // 载荷：用户名
                    .withClaim("password", token.getPassword())  // 载荷：密码哈希（注意：不建议放密码）
                    .withClaim("avatar", token.getAvatar())      // 载荷：头像URL
                    .withClaim("trueName", token.getTrueName())  // 载荷：真实姓名
                    .withExpiresAt(date)                         // 设置过期时间
                    .sign(algorithm);                            // 使用算法签名
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ============================================================
     * 验证并解析JWT Token
     * ============================================================
     * 工作流程：
     * 1. 使用相同的密钥和算法创建验证器
     * 2. 验证Token签名是否正确
     * 3. 验证Token是否过期
     * 4. 解析载荷，提取用户信息
     * 5. 封装为TokenVo对象返回
     * 
     * 验证失败的情况：
     * - Token被篡改：签名验证失败
     * - Token过期：超过5小时有效期
     * - Token格式错误：不是合法的JWT格式
     * - 密钥不匹配：使用了错误的密钥
     * 
     * 使用场景：
     * - 拦截器中验证请求的Token
     * - 从Token中获取当前登录用户信息
     * - 判断用户是否有权限访问资源
     * 
     * @param token JWT Token字符串（从请求头Authorization中获取）
     * @return TokenVo对象（包含用户信息），验证失败返回null
     */
    public static TokenVo verifyToken(String token) {
        try {
            // 1. 创建签名算法（必须与生成Token时使用的算法和密钥一致）
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            
            // 2. 创建JWT验证器
            JWTVerifier verifier = JWT.require(algorithm).build();
            
            // 3. 验证Token并解码
            //    - 验证签名：确保Token未被篡改
            //    - 验证过期时间：确保Token仍然有效
            DecodedJWT jwt = verifier.verify(token);
            
            // 4. 从载荷中提取用户信息
            String roleId = jwt.getClaim("roleId").asString();      // 角色ID
            String username = jwt.getClaim("username").asString();  // 用户名
            String password = jwt.getClaim("password").asString();  // 密码哈希
            String avatar = jwt.getClaim("avatar").asString();      // 头像URL
            String trueName = jwt.getClaim("trueName").asString();  // 真实姓名
            
            // 5. 封装为TokenVo对象返回
            return new TokenVo(roleId, username, password, avatar, trueName);
        } catch (Exception e) {
            // 验证失败：Token无效、过期或被篡改
            return null;
        }
    }

    // ========== 测试方法（已注释） ==========
    // 用于测试Token的生成和验证功能
//    public static void main(String[] args) {
//        // 创建Token
//        String token = TokenUtils.createToken(new TokenVo("3","wzz","9499273223c7aca5949e3055eaa57f6f"));
//        System.out.println("token == " + token);
//        
//        // 验证Token
//        TokenVo tokenVo = TokenUtils.verifyToken(token);
//        System.out.println(tokenVo);
//    }
}

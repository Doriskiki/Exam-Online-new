# JWT + SHA-256 实现文档

## 1. 业务场景

- 平台登录后前后端分离，使用 Token 鉴权，后端需校验用户身份与角色；Token 需具备过期时间。
- 用户密码（或密钥字段）需要加盐摘要，避免明文/弱摘要存储。

## 2. 技术方案

- JWT：`java-jwt`（HMAC-SHA256 对称签名），载荷含 `roleId/username/password/avatar/trueName`，默认过期 5 小时。
- 密码摘要：自定义加盐 + SHA-256（替换原 MD5），后端统一调用 `SaltEncryption.saltEncryption`。

## 3. 关键实现与代码

### 3.1 JWT 生成与校验

```java
// exam-admin/src/main/java/com/wzz/Util/TokenUtils.java
public class TokenUtils {
    private static final long EXPIRE_TIME = 60 * 60 * 1000 * 5; // 5小时
    private static final String TOKEN_SECRET = "wangzhouzhou"; // 对称密钥

    public static String createToken(TokenVo token) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        Map<String, Object> header = new HashMap<>(2);
        header.put("Type", "Jwt");
        header.put("alg", "HS256");
        return JWT.create()
                .withHeader(header)
                .withClaim("roleId", token.getRoleId())
                .withClaim("username", token.getUsername())
                .withClaim("password", token.getPassword())
                .withClaim("avatar", token.getAvatar())
                .withClaim("trueName", token.getTrueName())
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public static TokenVo verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String roleId = jwt.getClaim("roleId").asString();
        String username = jwt.getClaim("username").asString();
        String password = jwt.getClaim("password").asString();
        String avatar = jwt.getClaim("avatar").asString();
        String trueName = jwt.getClaim("trueName").asString();
        return new TokenVo(roleId, username, password, avatar, trueName);
    }
}
```

说明：对称密钥保存在代码中，生产应改为环境变量/配置中心；过期时间可调。

### 3.2 加盐 SHA-256 摘要

```java
// exam-admin/src/main/java/com/wzz/Util/SaltEncryption.java
public class SaltEncryption {
    public static String saltEncryption(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashed = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
```

## 4. API 使用流程

- 登录时：校验用户名/密码（后端将输入密码与用户盐拼接后做 SHA-256），成功后调用 `TokenUtils.createToken` 返回 Token。
- 鉴权时：前端在 `Authorization` 头携带 Token，后端拦截器/控制器调用 `TokenUtils.verifyToken` 获取用户信息与角色。

## 5. 遇到的问题与解决

- 对称密钥硬编码：目前使用常量，需在生产改为环境变量并定期轮换。
- 过期策略：默认 5 小时，如需短时会话可缩短并配合刷新 Token。
- SHA-256 输出长度：64 字符十六进制，确保数据库字段长度充足（建议 varchar(64+)）。

## 6. 优化方案

- Token 最小化：不在 Token 中放敏感字段（如密码），改为用户 ID + 角色，密码改为服务端存储的摘要不可逆。
- 密钥管理：引入 KMS/配置中心管理签名密钥；支持多密钥轮换（kid）。
- 刷新机制：Access Token + Refresh Token 双 token 策略，降低长期暴露风险。
- 摘要增强：可使用 PBKDF2/BCrypt/Argon2 代替简单 SHA-256，增加计算成本抵抗暴力破解。

## 7. 效果（定量估计）

- JWT 鉴权：服务端无会话存储，验证为纯计算，单次开销在微秒级，相比数据库会话查询延迟降低 1-2 个数量级。
- SHA-256：摘要计算微秒级，可承载高并发登录；更安全的哈希算法相较 MD5 抗碰撞性大幅提升。

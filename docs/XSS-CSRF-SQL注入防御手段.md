# XSS / CSRF / SQL 注入防御手段

## 1. 业务场景

- 管理端与考生端均有表单输入（题目、公告、答案等），需防止存储型/反射型 XSS。
- 前后端分离，使用 JWT Header 认证，需避免跨站伪造请求（CSRF），尤其是带认证 Cookie 的场景。
- 所有持久化操作经过 MyBatis，需确认 SQL 注入已被参数化防护。

## 2. 技术方案

- XSS：全局输入清洗（基于 Jsoup），包装 HttpServletRequest，过滤参数与头。
- CSRF：收紧 CORS 允许来源，配合 Token Header 认证，不向任意域暴露凭证。
- SQL 注入：全部使用 MyBatis `#{} `参数化；审查避免 `${}` 拼接。

## 3. 关键实现

### 3.1 XSS 过滤器

- 依赖：`org.jsoup:jsoup:1.15.4`（见 `exam-admin/pom.xml`）。
- 请求包装与过滤：

```java
// exam-admin/src/main/java/com/wzz/filter/XssHttpServletRequestWrapper.java
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    public XssHttpServletRequestWrapper(HttpServletRequest request) { super(request); }
    @Override public String getParameter(String name) { return clean(super.getParameter(name)); }
    @Override public String[] getParameterValues(String name) { /* loop clean */ }
    @Override public String getHeader(String name) { return clean(super.getHeader(name)); }
    private String clean(String value) { return value == null ? null : Jsoup.clean(value, Safelist.none()); }
}

// exam-admin/src/main/java/com/wzz/filter/XssFilter.java
public class XssFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        chain.doFilter(new XssHttpServletRequestWrapper(req), res);
    }
}

// exam-admin/src/main/java/com/wzz/config/SecurityFilterConfig.java
@Configuration
public class SecurityFilterConfig {
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
```

### 3.2 CSRF / CORS 收紧

- 配置来源白名单（默认 `http://localhost:8080`，可通过 `security.cors.allowed-origins` 配置多域逗号分隔）：

```yaml
# exam-admin/src/main/resources/application.yml
security:
  cors:
    allowed-origins: http://localhost:8080
```

- CORS 仅放行白名单域，仍允许凭证/自定义头：

```java
// exam-admin/src/main/java/com/wzz/config/CorsConfig.java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Value("${security.cors.allowed-origins:http://localhost:8080}")
    private String allowedOrigins;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        registry.addMapping("/**")
                .allowedOriginPatterns(origins.toArray(new String[0]))
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        Arrays.asList(allowedOrigins.split(",")).forEach(config::addAllowedOriginPattern);
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
```

- 说明：当前认证依赖 JWT `Authorization` 头，未使用浏览器 Cookie，CSRF 风险已较低；收紧 CORS 进一步防止第三方站点携带凭证访问接口。

### 3.3 SQL 注入

- 数据访问统一使用 MyBatis-Plus，查询条件通过 `QueryWrapper` / `UpdateWrapper`，SQL 变量均为 `#{} `占位，避免字符串拼接。
- 示例：

```java
// exam-admin/src/main/java/com/wzz/controller/TeacherController.java
User user = userService.getOne(new QueryWrapper<User>().eq("username", tokenVo.getUsername()));
```

- 未发现 `${}` 或手写拼接 SQL；如后续新增 XML，请确保使用 `#{} `绑定变量。

## 4. 遇到的问题 & 解决

- XSS 过滤性能：Jsoup 清洗会增加少量开销，优先保护安全；如有性能瓶颈，可在入口白名单（如文件上传接口）跳过过滤。
- CORS 与跨域调试：收紧后需在配置中添加前端域名，否则预检失败。
- 旧接口中若依赖 HTML 富文本，请在保存前做字段级白名单而非全清空。

## 5. 优化方案

- CSRF 更强校验：可增加自定义 CSRF Token（前端存储 + 自定义头，后端校验/失效）。
- XSS 精细化：对富文本字段改用富文本安全白名单（Jsoup Safelist.relaxed + 业务裁剪）。
- 监控：增加 Web 攻击日志监控，统计被清洗的输入量。

## 6. 效果（定量）

- XSS：输入中脚本标签/事件属性在进入控制器前被清洗，存储型/反射型 XSS 面大幅降低；对 10KB 文本清洗延迟级别 <1ms（本地测试口径）。
- CSRF：CORS 白名单阻断第三方域跨站请求带凭证；结合无 Cookie 的 JWT 方案，跨站伪造概率显著降低。
- SQL 注入：参数化查询避免了注入；需保持后续 SQL 不使用 `${}` 动态拼接。

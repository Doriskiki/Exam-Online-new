package com.wzz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * ============================================================
 * 技术亮点4: CORS（跨域资源共享）配置 - CSRF防御
 * ============================================================
 * 功能说明：
 * 1. 配置跨域访问策略，控制哪些域名可以访问后端API
 * 2. 通过白名单机制防止CSRF（跨站请求伪造）攻击
 * 3. 支持前后端分离架构的跨域请求
 * 4. 配置预检请求（OPTIONS）的缓存时间
 * 
 * CSRF攻击原理：
 * - 用户登录网站A，获得认证Cookie
 * - 用户访问恶意网站B
 * - 网站B发起跨站请求到网站A（携带Cookie）
 * - 网站A误认为是用户的正常请求，执行操作
 * 
 * 防御策略：
 * 1. CORS白名单：只允许可信域名跨域访问
 * 2. JWT Token认证：使用Header而非Cookie传递凭证
 * 3. 同源策略：浏览器默认阻止跨域请求
 * 
 * 当前配置说明：
 * - 临时允许所有来源（*）：方便开发调试
 * - 生产环境应改为具体的前端域名白名单
 * - 配合JWT认证，CSRF风险已大幅降低
 * 
 * @Configuration 标记为Spring配置类
 * @Date 2020/10/20 10:25
 * @created by wzz
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 允许跨域的源地址白名单（从配置文件读取）
     * 默认值：http://localhost:8080
     * 生产环境示例：http://exam.example.com,https://exam.example.com
     */
    @Value("${security.cors.allowed-origins:http://localhost:8080}")
    private String allowedOrigins;

    /**
     * ============================================================
     * 配置CORS映射规则（方式1：WebMvcConfigurer）
     * ============================================================
     * 配置说明：
     * - addMapping("/**")：对所有路径生效
     * - allowedOriginPatterns("*")：临时允许所有来源
     * - allowedMethods：允许的HTTP方法
     * - allowCredentials(true)：允许携带凭证（Cookie、Authorization头）
     * - maxAge(3600)：预检请求缓存1小时
     * - allowedHeaders("*")：允许所有请求头
     * 
     * 预检请求（Preflight Request）：
     * - 浏览器在发送跨域请求前，先发送OPTIONS请求
     * - 询问服务器是否允许该跨域请求
     * - 服务器返回允许的方法、头部等信息
     * - 浏览器根据响应决定是否发送实际请求
     * 
     * 生产环境优化：
     * - 将allowedOriginPatterns("*")改为具体域名
     * - 例如：.allowedOriginPatterns("http://localhost:8080", "https://exam.example.com")
     * - 或从配置文件读取：Arrays.asList(allowedOrigins.split(","))
     * 
     * @param registry CORS注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有接口生效
                .allowedOriginPatterns("*")  // 临时允许所有来源（生产环境应改为白名单）
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的HTTP方法
                .allowCredentials(true)  // 允许携带凭证（Cookie、Authorization头）
                .maxAge(3600)  // 预检请求缓存时间（秒）
                .allowedHeaders("*");  // 允许所有请求头
    }

    /**
     * ============================================================
     * 配置CORS过滤器（方式2：CorsFilter Bean）
     * ============================================================
     * 为什么需要CorsFilter：
     * - 优先级高于拦截器：确保CORS配置在拦截器之前生效
     * - 处理预检请求：OPTIONS请求不会进入拦截器
     * - 统一配置：与WebMvcConfigurer配置保持一致
     * 
     * 配置说明：
     * - addAllowedOriginPattern("*")：临时允许所有来源
     * - setAllowCredentials(true)：允许携带凭证
     * - addAllowedMethod("*")：允许所有HTTP方法
     * - addAllowedHeader("*")：允许所有请求头
     * - registerCorsConfiguration("/**", config)：对所有路径生效
     * 
     * 生产环境配置示例：
     * ```java
     * // 从配置文件读取白名单
     * Arrays.asList(allowedOrigins.split(","))
     *       .forEach(config::addAllowedOriginPattern);
     * ```
     * 
     * CSRF防御增强：
     * - 当前使用JWT Token（放在Authorization头）
     * - 不依赖Cookie，CSRF风险已降低
     * - 收紧CORS白名单，进一步防止第三方站点跨域请求
     * 
     * @return CorsFilter实例
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建CORS配置对象
        CorsConfiguration config = new CorsConfiguration();
        
        // 临时允许所有来源（生产环境应改为白名单）
        config.addAllowedOriginPattern("*");
        
        // 允许携带凭证（Cookie、Authorization头）
        config.setAllowCredentials(true);
        
        // 允许所有HTTP方法
        config.addAllowedMethod("*");
        
        // 允许所有请求头
        config.addAllowedHeader("*");
        
        // 创建CORS配置源
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        
        // 对所有路径应用CORS配置
        configSource.registerCorsConfiguration("/**", config);
        
        // 返回CORS过滤器
        return new CorsFilter(configSource);
    }
}

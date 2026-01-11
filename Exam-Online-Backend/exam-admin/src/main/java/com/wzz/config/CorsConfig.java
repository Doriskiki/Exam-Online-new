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

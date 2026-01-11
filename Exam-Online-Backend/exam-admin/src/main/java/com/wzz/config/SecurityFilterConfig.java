package com.wzz.config;

import com.wzz.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ============================================================
 * 技术亮点4: 安全过滤器配置 - XSS防御
 * ============================================================
 * 功能说明：
 * 1. 注册XSS过滤器到Spring容器
 * 2. 配置过滤器的URL匹配模式
 * 3. 设置过滤器的执行顺序
 * 4. 确保XSS过滤在所有请求处理前执行
 * 
 * 过滤器执行顺序（Order）：
 * - Order值越小，优先级越高
 * - 当前设置为1，优先级很高
 * - 确保在业务逻辑前完成XSS清洗
 * 
 * 过滤器链示例：
 * 1. XssFilter (Order=1) - XSS清洗
 * 2. CorsFilter (Order=默认) - CORS处理
 * 3. 其他过滤器
 * 4. 拦截器
 * 5. 控制器
 * 
 * @Configuration 标记为Spring配置类
 */
@Configuration
public class SecurityFilterConfig {

    /**
     * ============================================================
     * 注册XSS过滤器
     * ============================================================
     * FilterRegistrationBean作用：
     * - 将自定义过滤器注册到Spring容器
     * - 配置过滤器的URL匹配模式
     * - 设置过滤器的执行顺序
     * - 管理过滤器的生命周期
     * 
     * 配置说明：
     * - setFilter(new XssFilter())：设置过滤器实例
     * - addUrlPatterns("/*")：拦截所有请求
     * - setOrder(1)：设置优先级为1（数字越小优先级越高）
     * 
     * URL模式说明：
     * - "/*"：匹配所有一级路径（/user、/exam等）
     * - "/**"：匹配所有路径（包括多级路径）
     * - 当前使用"/*"已足够覆盖所有接口
     * 
     * 为什么需要高优先级：
     * - 在业务逻辑前清洗XSS
     * - 确保进入控制器的参数都是安全的
     * - 避免恶意脚本进入数据库
     * 
     * 可选优化：
     * - 针对特定接口跳过XSS过滤（如文件上传）
     * - 使用excludeUrlPatterns()排除特定路径
     * - 例如：registration.addExcludeUrlPatterns("/upload/*");
     * 
     * @return FilterRegistrationBean<XssFilter> 过滤器注册Bean
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration() {
        // 创建过滤器注册Bean
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        
        // 设置过滤器实例
        registration.setFilter(new XssFilter());
        
        // 设置URL匹配模式：拦截所有请求
        registration.addUrlPatterns("/*");
        
        // 设置过滤器执行顺序：1（优先级很高）
        registration.setOrder(1);
        
        return registration;
    }
}

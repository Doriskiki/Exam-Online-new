package com.wzz.filter;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * ============================================================
 * 技术亮点4: XSS防御 - HTTP请求包装类
 * ============================================================
 * 功能说明：
 * 1. 继承HttpServletRequestWrapper，重写参数获取方法
 * 2. 对所有输入参数进行XSS清洗（使用Jsoup库）
 * 3. 清除HTML标签、脚本代码、事件属性等危险内容
 * 4. 保证进入控制器的参数都是安全的
 * 
 * 包装器模式（Wrapper Pattern）：
 * - 不修改原始HttpServletRequest
 * - 通过包装类增强功能（添加XSS清洗）
 * - 对外接口保持不变，透明替换
 * 
 * 清洗策略：
 * - 使用Jsoup.clean()方法
 * - Safelist.none()：移除所有HTML标签
 * - 只保留纯文本内容
 * 
 * 清洗示例：
 * 输入：<script>alert('XSS')</script>Hello
 * 输出：Hello
 * 
 * 输入：<img src=x onerror=alert('XSS')>
 * 输出：（空字符串）
 * 
 * 输入：<a href="javascript:alert('XSS')">点击</a>
 * 输出：点击
 * 
 * 性能考虑：
 * - Jsoup清洗速度快（微秒级）
 * - 只在获取参数时清洗，不影响整体性能
 * - 可以针对特定接口跳过清洗（如文件上传）
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 构造函数：接收原始请求对象
     * 
     * @param request 原始HTTP请求
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * ============================================================
     * 重写getParameter方法：清洗单个参数
     * ============================================================
     * 使用场景：
     * - 控制器中使用@RequestParam获取参数
     * - 例如：String username = request.getParameter("username");
     * 
     * 清洗流程：
     * 1. 调用父类方法获取原始参数值
     * 2. 使用clean()方法清洗XSS
     * 3. 返回清洗后的安全参数
     * 
     * @param name 参数名
     * @return 清洗后的参数值
     */
    @Override
    public String getParameter(String name) {
        return clean(super.getParameter(name));
    }

    /**
     * ============================================================
     * 重写getParameterValues方法：清洗参数数组
     * ============================================================
     * 使用场景：
     * - 复选框、多选下拉框等多值参数
     * - 例如：String[] hobbies = request.getParameterValues("hobby");
     * 
     * 清洗流程：
     * 1. 获取原始参数数组
     * 2. 遍历数组，逐个清洗
     * 3. 返回清洗后的数组
     * 
     * @param name 参数名
     * @return 清洗后的参数数组
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        // 创建新数组存储清洗后的值
        String[] cleaned = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            cleaned[i] = clean(values[i]);
        }
        return cleaned;
    }

    /**
     * ============================================================
     * 重写getHeader方法：清洗请求头
     * ============================================================
     * 使用场景：
     * - 防止通过请求头注入XSS
     * - 例如：User-Agent、Referer等自定义头
     * 
     * 注意事项：
     * - Authorization头也会被清洗，但JWT Token通常不包含HTML
     * - 如果需要，可以针对特定头跳过清洗
     * 
     * @param name 请求头名称
     * @return 清洗后的请求头值
     */
    @Override
    public String getHeader(String name) {
        return clean(super.getHeader(name));
    }

    /**
     * ============================================================
     * 核心清洗方法：使用Jsoup清除XSS
     * ============================================================
     * Jsoup.clean()方法说明：
     * - 参数1：待清洗的字符串
     * - 参数2：白名单策略（Safelist）
     * 
     * Safelist策略：
     * - Safelist.none()：移除所有HTML标签，只保留文本
     * - Safelist.basic()：保留基本格式标签（b、i、u等）
     * - Safelist.relaxed()：保留更多标签（适合富文本）
     * 
     * 当前使用Safelist.none()的原因：
     * - 最严格的策略，安全性最高
     * - 适合普通表单输入（用户名、密码、题目内容等）
     * - 如果需要富文本，可以针对特定字段使用relaxed()
     * 
     * 清洗效果：
     * - 移除所有HTML标签：<div>、<script>、<img>等
     * - 移除事件属性：onclick、onerror、onload等
     * - 移除危险协议：javascript:、data:等
     * - 保留纯文本内容
     * 
     * @param value 待清洗的字符串
     * @return 清洗后的安全字符串，输入为null时返回null
     */
    private String clean(String value) {
        if (value == null) {
            return null;
        }
        // 使用Jsoup清洗XSS，移除所有HTML标签
        return Jsoup.clean(value, Safelist.none());
    }
}

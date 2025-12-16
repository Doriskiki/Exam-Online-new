package com.wzz.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/10/20 08:54
 * @Author wzz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String message;
    private T data;

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "操作成功", data);
    }

    /**
     * 成功返回（带数据和消息）
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<>(200, message, data);
    }

    /**
     * 成功返回（仅消息）
     */
    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<>(200, message, null);
    }

    /**
     * 失败返回
     */
    public static <T> CommonResult<T> error(String message) {
        return new CommonResult<>(500, message, null);
    }

    /**
     * 失败返回（带错误码）
     */
    public static <T> CommonResult<T> error(Integer code, String message) {
        return new CommonResult<>(code, message, null);
    }
}

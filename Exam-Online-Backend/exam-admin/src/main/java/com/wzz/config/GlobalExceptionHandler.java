package com.wzz.config;

import com.wzz.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for all controllers
 * Provides centralized error handling and logging
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle NullPointerException
     * @param e NullPointerException
     * @return CommonResult with error message
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<String> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException occurred: ", e);
        return new CommonResult<>(500, "服务器内部错误：空指针异常");
    }

    /**
     * Handle validation exceptions
     * @param e MethodArgumentNotValidException
     * @return CommonResult with validation error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation error occurred: ", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new CommonResult<>(400, "参数验证失败", errors);
    }

    /**
     * Handle IllegalArgumentException
     * @param e IllegalArgumentException
     * @return CommonResult with error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException occurred: ", e);
        return new CommonResult<>(400, "参数错误：" + e.getMessage());
    }

    /**
     * Handle all other exceptions
     * @param e Exception
     * @return CommonResult with generic error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<String> handleGeneralException(Exception e) {
        log.error("Unexpected exception occurred: ", e);
        return new CommonResult<>(500, "服务器内部错误，请稍后重试");
    }
}

package com.wzz.vo;

import lombok.Data;

/**
 * 人脸识别返回对象
 */
@Data
public class FaceRecognizeVo {
    private Boolean success;
    private Boolean matched;
    private String userId;
    private String userName;
    private Double similarity;
    private Double threshold;
    private String message;
}

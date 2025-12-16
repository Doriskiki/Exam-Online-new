package com.wzz.vo;

import lombok.Data;
import java.util.List;

/**
 * 人脸注册返回对象
 */
@Data
public class FaceRegisterVo {
    private Boolean success;
    private String message;
    private String userId;
    private String userName;
    private List<Double> embedding;
}

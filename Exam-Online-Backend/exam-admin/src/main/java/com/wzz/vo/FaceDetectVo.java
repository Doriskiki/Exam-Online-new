package com.wzz.vo;

import lombok.Data;
import java.util.List;

/**
 * 人脸检测返回对象
 */
@Data
public class FaceDetectVo {
    private Boolean success;
    private Integer count;
    private List<FaceInfo> faces;
    private String message;
    
    @Data
    public static class FaceInfo {
        private List<Integer> bbox;  // [x1, y1, x2, y2]
        private Double confidence;
        private List<List<Integer>> landmarks;
    }
}

package com.wzz.service;

import com.wzz.vo.FaceDetectVo;
import com.wzz.vo.FaceRecognizeVo;
import com.wzz.vo.FaceRegisterVo;

/**
 * 人脸识别服务接口
 */
public interface FaceRecognitionService {
    
    /**
     * 人脸检测
     * @param imageBase64 base64编码的图片
     * @return 检测结果
     */
    FaceDetectVo detectFace(String imageBase64);
    
    /**
     * 人脸注册
     * @param userId 用户ID
     * @param userName 用户名
     * @param imageBase64 base64编码的图片
     * @return 注册结果
     */
    FaceRegisterVo registerFace(Integer userId, String userName, String imageBase64);
    
    /**
     * 人脸识别验证
     * @param imageBase64 base64编码的图片
     * @param threshold 相似度阈值（0-1之间，默认0.5）
     * @return 识别结果
     */
    FaceRecognizeVo recognizeFace(String imageBase64, Double threshold);
    
    /**
     * 删除用户人脸数据
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean deleteFace(Integer userId);
    
    /**
     * 考试前人脸验证
     * @param userId 用户ID
     * @param examId 考试ID
     * @param imageBase64 base64编码的图片
     * @return 验证结果
     */
    FaceRecognizeVo verifyForExam(Integer userId, Integer examId, String imageBase64);
}

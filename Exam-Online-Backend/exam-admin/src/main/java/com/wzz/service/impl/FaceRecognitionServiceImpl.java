package com.wzz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wzz.entity.FaceRecord;
import com.wzz.mapper.FaceRecordMapper;
import com.wzz.service.FaceRecognitionService;
import com.wzz.vo.FaceDetectVo;
import com.wzz.vo.FaceRecognizeVo;
import com.wzz.vo.FaceRegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 人脸识别服务实现类
 */
@Slf4j
@Service
public class FaceRecognitionServiceImpl implements FaceRecognitionService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private FaceRecordMapper faceRecordMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${face.service.url:http://localhost:5000}")
    private String faceServiceUrl;
    
    @Override
    public FaceDetectVo detectFace(String imageBase64) {
        try {
            String url = faceServiceUrl + "/api/face/detect";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("image", imageBase64);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<FaceDetectVo> response = restTemplate.postForEntity(
                url, entity, FaceDetectVo.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("人脸检测失败", e);
            FaceDetectVo result = new FaceDetectVo();
            result.setSuccess(false);
            result.setMessage("人脸检测服务异常: " + e.getMessage());
            return result;
        }
    }
    
    @Override
    public FaceRegisterVo registerFace(Integer userId, String userName, String imageBase64) {
        try {
            String url = faceServiceUrl + "/api/face/register";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", userId.toString());
            requestBody.put("userName", userName);
            requestBody.put("image", imageBase64);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<FaceRegisterVo> response = restTemplate.postForEntity(
                url, entity, FaceRegisterVo.class
            );
            
            FaceRegisterVo result = response.getBody();
            
            // 保存注册记录到数据库
            if (result != null && result.getSuccess()) {
                FaceRecord record = new FaceRecord();
                record.setUserId(userId);
                record.setRecordType("register");
                record.setMatched(true);
                record.setRecordTime(LocalDateTime.now());
                record.setRemark("人脸注册成功");
                
                // 保存特征向量
                if (result.getEmbedding() != null) {
                    record.setEmbedding(objectMapper.writeValueAsString(result.getEmbedding()));
                }
                
                faceRecordMapper.insert(record);
            }
            
            return result;
        } catch (Exception e) {
            log.error("人脸注册失败", e);
            FaceRegisterVo result = new FaceRegisterVo();
            result.setSuccess(false);
            result.setMessage("人脸注册服务异常: " + e.getMessage());
            return result;
        }
    }
    
    @Override
    public FaceRecognizeVo recognizeFace(String imageBase64, Double threshold) {
        try {
            String url = faceServiceUrl + "/api/face/recognize";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("image", imageBase64);
            if (threshold != null) {
                requestBody.put("threshold", threshold);
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<FaceRecognizeVo> response = restTemplate.postForEntity(
                url, entity, FaceRecognizeVo.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            log.error("人脸识别失败", e);
            FaceRecognizeVo result = new FaceRecognizeVo();
            result.setSuccess(false);
            result.setMessage("人脸识别服务异常: " + e.getMessage());
            return result;
        }
    }
    
    @Override
    public Boolean deleteFace(Integer userId) {
        try {
            String url = faceServiceUrl + "/api/face/delete";
            
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userId", userId.toString());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(
                url, entity, Map.class
            );
            
            Map<String, Object> result = response.getBody();
            return result != null && (Boolean) result.get("success");
        } catch (Exception e) {
            log.error("删除人脸数据失败", e);
            return false;
        }
    }
    
    @Override
    public FaceRecognizeVo verifyForExam(Integer userId, Integer examId, String imageBase64) {
        try {
            // 先进行人脸识别
            FaceRecognizeVo result = recognizeFace(imageBase64, 0.6);
            
            // 保存验证记录
            FaceRecord record = new FaceRecord();
            record.setUserId(userId);
            record.setExamId(examId);
            record.setRecordType("verify");
            record.setRecordTime(LocalDateTime.now());
            
            if (result != null && result.getSuccess()) {
                if (result.getMatched()) {
                    // 验证识别出的用户ID是否匹配
                    if (userId.toString().equals(result.getUserId())) {
                        record.setMatched(true);
                        record.setSimilarity(result.getSimilarity());
                        record.setRemark("考试前人脸验证通过");
                    } else {
                        record.setMatched(false);
                        record.setRemark("识别到的用户ID不匹配");
                        result.setMatched(false);
                        result.setMessage("人脸验证失败：识别到的身份与当前用户不匹配");
                    }
                } else {
                    // 未匹配到人脸
                    record.setMatched(false);
                    record.setRemark("未找到匹配的人脸");
                    result.setMessage("人脸验证失败：您还未注册人脸信息，请先在个人中心注册人脸");
                }
            } else {
                record.setMatched(false);
                record.setRemark("人脸验证失败");
                if (result == null) {
                    result = new FaceRecognizeVo();
                    result.setSuccess(false);
                    result.setMessage("人脸识别服务无响应");
                }
            }
            
            faceRecordMapper.insert(record);
            return result;
        } catch (Exception e) {
            log.error("考试人脸验证失败", e);
            FaceRecognizeVo result = new FaceRecognizeVo();
            result.setSuccess(false);
            result.setMessage("人脸验证服务异常: " + e.getMessage());
            return result;
        }
    }
}

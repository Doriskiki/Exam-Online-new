package com.wzz.controller;

import com.wzz.service.FaceRecognitionService;
import com.wzz.vo.CommonResult;
import com.wzz.vo.FaceDetectVo;
import com.wzz.vo.FaceRecognizeVo;
import com.wzz.vo.FaceRegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 人脸识别控制器
 */
@Slf4j
@RestController
@RequestMapping("/face")
@Api(tags = "人脸识别接口")
public class FaceRecognitionController {
    
    @Autowired
    private FaceRecognitionService faceRecognitionService;
    
    @PostMapping("/detect")
    @ApiOperation("人脸检测")
    public CommonResult<FaceDetectVo> detectFace(
            @ApiParam("base64编码的图片") @RequestBody Map<String, String> request) {
        try {
            String imageBase64 = request.get("image");
            if (imageBase64 == null || imageBase64.isEmpty()) {
                return CommonResult.error("图片数据不能为空");
            }
            
            FaceDetectVo result = faceRecognitionService.detectFace(imageBase64);
            
            if (result.getSuccess()) {
                return CommonResult.success(result, result.getMessage());
            } else {
                return CommonResult.error(result.getMessage());
            }
        } catch (Exception e) {
            log.error("人脸检测异常", e);
            return CommonResult.error("人脸检测失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/register")
    @ApiOperation("人脸注册")
    public CommonResult<FaceRegisterVo> registerFace(
            @ApiParam("用户ID") @RequestParam Integer userId,
            @ApiParam("用户名") @RequestParam String userName,
            @ApiParam("base64编码的图片") @RequestBody Map<String, String> request) {
        try {
            String imageBase64 = request.get("image");
            if (imageBase64 == null || imageBase64.isEmpty()) {
                return CommonResult.error("图片数据不能为空");
            }
            
            FaceRegisterVo result = faceRecognitionService.registerFace(userId, userName, imageBase64);
            
            if (result.getSuccess()) {
                return CommonResult.success(result, result.getMessage());
            } else {
                return CommonResult.error(result.getMessage());
            }
        } catch (Exception e) {
            log.error("人脸注册异常", e);
            return CommonResult.error("人脸注册失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/recognize")
    @ApiOperation("人脸识别")
    public CommonResult<FaceRecognizeVo> recognizeFace(
            @ApiParam("base64编码的图片") @RequestBody Map<String, Object> request) {
        try {
            String imageBase64 = (String) request.get("image");
            if (imageBase64 == null || imageBase64.isEmpty()) {
                return CommonResult.error("图片数据不能为空");
            }
            
            Double threshold = request.get("threshold") != null ? 
                    Double.parseDouble(request.get("threshold").toString()) : 0.5;
            
            FaceRecognizeVo result = faceRecognitionService.recognizeFace(imageBase64, threshold);
            
            if (result.getSuccess()) {
                return CommonResult.success(result, result.getMessage());
            } else {
                return CommonResult.error(result.getMessage());
            }
        } catch (Exception e) {
            log.error("人脸识别异常", e);
            return CommonResult.error("人脸识别失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/verify-exam")
    @ApiOperation("考试前人脸验证")
    public CommonResult<FaceRecognizeVo> verifyForExam(
            @ApiParam("用户ID") @RequestParam Integer userId,
            @ApiParam("考试ID") @RequestParam Integer examId,
            @ApiParam("base64编码的图片") @RequestBody Map<String, String> request) {
        try {
            String imageBase64 = request.get("image");
            if (imageBase64 == null || imageBase64.isEmpty()) {
                return CommonResult.error("图片数据不能为空");
            }
            
            FaceRecognizeVo result = faceRecognitionService.verifyForExam(userId, examId, imageBase64);
            
            if (result.getSuccess() && result.getMatched()) {
                return CommonResult.success(result, "人脸验证通过，可以开始考试");
            } else {
                return CommonResult.error(result.getMessage() != null ? 
                        result.getMessage() : "人脸验证失败，无法开始考试");
            }
        } catch (Exception e) {
            log.error("考试人脸验证异常", e);
            return CommonResult.error("人脸验证失败: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/delete/{userId}")
    @ApiOperation("删除用户人脸数据")
    public CommonResult<Boolean> deleteFace(
            @ApiParam("用户ID") @PathVariable Integer userId) {
        try {
            Boolean result = faceRecognitionService.deleteFace(userId);
            
            if (result) {
                return CommonResult.success(true, "人脸数据删除成功");
            } else {
                return CommonResult.error("人脸数据删除失败");
            }
        } catch (Exception e) {
            log.error("删除人脸数据异常", e);
            return CommonResult.error("删除失败: " + e.getMessage());
        }
    }
}

package com.wzz.common;

import com.wzz.Util.OSSUtil;
import com.wzz.Util.UploadUtils;
import com.wzz.entity.User;
import com.wzz.service.impl.UserServiceImpl;
import com.wzz.vo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/upload")
@Slf4j
public class Upload {

    @Autowired
    private UploadUtils uploadUtils;

//    @Autowired
//    private RedisUtils redisUtils;

    @Autowired
    private UserServiceImpl userService;

    /**
     * 处理文件上传请求
     * 该方法使用@PostMapping注解，表示它仅响应POST请求
     * 主要功能是接收用户上传的文件，并将其存储到指定位置，然后返回文件的访问路径
     *
     * @param fileType 文件类型，用于确定文件存储的目录或处理方式
     * @param file 用户上传的文件，封装为MultipartFile类型
     * @return 返回一个Result对象，泛型为String，通常包含文件的访问路径或存储状态
     */
    @PostMapping
    public CommonResult<String> upload(String fileType, MultipartFile file) {
        log.info("收到文件上传请求，fileType: {}, fileName: {}", fileType, file.getOriginalFilename());
        try {
            // 如果是考试用户截图，使用OSS上传
            if ("examUserImg".equals(fileType)) {
                log.info("使用OSS上传诚信截图");
                String url = OSSUtil.picOSS(file);
                log.info("OSS上传成功，URL: {}", url);
                return new CommonResult<>(200, "上传成功", url);
            }
            
            // 其他文件类型使用本地上传
            log.info("使用本地上传，fileType: {}", fileType);
            String fileName = uploadUtils.upload(file, fileType);
            log.info("本地上传成功，fileName: {}", fileName);
            return new CommonResult<>(200, "上传成功", fileName);
        } catch (Exception e) {
            log.error("文件上传失败，fileType: {}, fileName: {}", fileType, file.getOriginalFilename(), e);
            return new CommonResult<>(500, "上传失败: " + e.getMessage(), null);
        }
    }
}

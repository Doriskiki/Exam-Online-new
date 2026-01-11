package com.wzz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzz.entity.Exam;
import com.wzz.mapper.ExamMapper;
import com.wzz.service.ExamService;
import org.springframework.stereotype.Service;

/**
 * @created by wzz
 */
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {
}

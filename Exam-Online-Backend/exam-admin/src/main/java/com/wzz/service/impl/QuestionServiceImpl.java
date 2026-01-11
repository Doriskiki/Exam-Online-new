package com.wzz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzz.entity.Question;
import com.wzz.mapper.QuestionMapper;
import com.wzz.service.QuestionService;
import org.springframework.stereotype.Service;

/**
 * @created by wzz
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
}

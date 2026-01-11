package com.wzz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzz.entity.Answer;
import com.wzz.mapper.AnswerMapper;
import com.wzz.service.AnswerService;
import org.springframework.stereotype.Service;

/**
 * @created by wzz
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
}

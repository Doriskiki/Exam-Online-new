package com.wzz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzz.entity.ExamQuestion;
import com.wzz.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @created by wzz
 */
//在对应的mapper上面实现基本的接口
@Repository//代表持久层
public interface ExamQuestionMapper extends BaseMapper<ExamQuestion> {
}

package com.wzz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzz.entity.FaceRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人脸识别记录Mapper
 */
@Mapper
public interface FaceRecordMapper extends BaseMapper<FaceRecord> {
}

package com.dormManager.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dormManager.backend.entity.dto.StudentBed;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentDormitoryMapper extends BaseMapper<StudentBed> {
}

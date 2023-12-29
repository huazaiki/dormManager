package com.dormManager.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dormManager.backend.entity.dto.Student;
import com.dormManager.backend.entity.dto.StudentDormitory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}

package com.dormManager.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dormManager.backend.entity.dto.Student;
import com.dormManager.backend.entity.dto.StudentBed;
import com.dormManager.backend.entity.vo.response.StudentVO;

import java.util.List;

public interface StudentBedService extends IService<StudentBed> {

    public String assignDormitory(Integer student_id, Integer dormitory_id);

    public List<StudentVO> getDormitoryInfo(Integer dormitory_id);
}

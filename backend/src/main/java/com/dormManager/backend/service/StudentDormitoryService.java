package com.dormManager.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dormManager.backend.entity.dto.StudentBed;
import com.dormManager.backend.entity.vo.response.StudentVO;

import java.util.List;

public interface StudentDormitoryService extends IService<StudentBed> {

    public String assignDormitory(String student_number, String dormitory_number);

    public List<StudentVO> getDormitoryInfo(String building_number, String dormitory_number);
}

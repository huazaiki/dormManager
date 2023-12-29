package com.dormManager.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dormManager.backend.entity.dto.Student;
import com.dormManager.backend.entity.vo.response.StudentVO;

public interface StudentService extends IService<Student> {

    StudentVO queryStudentInfo(Integer sid);

    String addStudent(Student student);

    String deleteStudent(Integer student_id);
}

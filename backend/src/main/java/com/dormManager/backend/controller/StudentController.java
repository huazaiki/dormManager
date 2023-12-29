package com.dormManager.backend.controller;

import com.dormManager.backend.entity.RestBean;
import com.dormManager.backend.entity.dto.Student;
import com.dormManager.backend.entity.vo.response.StudentVO;
import com.dormManager.backend.service.StudentDormitoryService;
import com.dormManager.backend.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stu")
public class StudentController {

    @Resource
    StudentService studentService;

    @Resource
    StudentDormitoryService studentDormitoryService;

    @GetMapping("/queryStudent")
    public RestBean<StudentVO> queryStudent(@RequestParam Integer sid){
        StudentVO vo =studentService.queryStudentInfo(sid);
        return RestBean.success(vo);
    }

    @PostMapping("/addStudent")
    public RestBean<String> addStudent(@RequestBody Student addStudent){
        String msg = studentService.addStudent(addStudent);
        return msg ==null ? RestBean.success() : RestBean.failure(401,msg);
    }

    @DeleteMapping("/deleteStudent")
    public RestBean<String> deleteStudent(@RequestParam Integer student_id){
        String msg = studentService.deleteStudent(student_id);
        return msg ==null ? RestBean.success() : RestBean.failure(401,msg);
    }

    @PostMapping("/updateStudent")
    public RestBean<String> updateStudent(@RequestBody Student student){
        String msg = studentService.updateStudent(student);
        return msg ==null ? RestBean.success() : RestBean.failure(401,msg);
    }

    @PostMapping("/assignDormitory")
    public RestBean<String> assignDormitory(@RequestParam String student_number,
                                            @RequestParam String dormitory_number){
        String msg = studentDormitoryService.assignDormitory(student_number,dormitory_number);
        return msg ==null ? RestBean.success() : RestBean.failure(401,msg);
    }

}

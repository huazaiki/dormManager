package com.dormManager.backend.controller.admin;

import com.dormManager.backend.entity.RestBean;
import com.dormManager.backend.entity.vo.response.StudentVO;
import com.dormManager.backend.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Resource
    StudentService studentService;

    @Resource
    StudentDormitoryService studentDormitoryService;



    @GetMapping("/searchStudentInfo")
    public RestBean<StudentVO> searchStudentInfo(@RequestParam Integer sid) {
        StudentVO vo = studentService.queryStudentInfo(sid);
        if (vo != null) return RestBean.success(vo);
        else return RestBean.failure(401, "请求参数有误");
    }

    
    @GetMapping("/searchDormitoyInfo")
    public RestBean<List<StudentVO>> searchDormitoryInfo(@RequestParam String dormNumber ,String buildNum) {
        List<StudentVO> studentVOS = studentDormitoryService.queryDormitoryInfo(dormNumber, buildNum);
        if (studentVOS != null) return RestBean.success(studentVOS);
        else return RestBean.failure(401, "请求参数有误");
    }

    @PostMapping("/addStudentInfo")
    public RestBean<List<StudentVO>> addStudentInfo(@RequestParam Student student){
        StudentVO vo = studentService
    }
}

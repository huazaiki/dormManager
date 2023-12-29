package com.dormManager.backend.controller.user;

import com.dormManager.backend.entity.RestBean;
import com.dormManager.backend.entity.vo.response.StudentVO;
import com.dormManager.backend.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/User")
public class UserApiController {
    @Resource
    StudentService studentService;
    @GetMapping("/searchStudentInfo")
    public RestBean<StudentVO>searchStudentInfo(@RequestParam Integer sid){
        StudentVO vo = studentService.queryStudentInfo(sid);
        if (vo != null) return RestBean.success(vo);
        else return RestBean.failure(401, "请求参数有误");
    }
}

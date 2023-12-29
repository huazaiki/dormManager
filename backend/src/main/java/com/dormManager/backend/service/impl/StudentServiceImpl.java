package com.dormManager.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormManager.backend.entity.dto.Dormitory;
import com.dormManager.backend.entity.dto.Student;
import com.dormManager.backend.entity.dto.StudentBed;
import com.dormManager.backend.entity.vo.response.StudentVO;
import com.dormManager.backend.mapper.StudentMapper;
import com.dormManager.backend.service.DormitoryService;
import com.dormManager.backend.service.StudentDormitoryService;
import com.dormManager.backend.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    StudentMapper stuMapper;

    @Resource
    DormitoryService dormitoryService;

    @Resource
    StudentDormitoryService studentBedService;

    /**
     * 查询学生的详细信息，包括姓名、所在宿舍号和床位号。
     *
     * @param sid 学生ID
     * @return 包含学生详细信息的StudentVO对象，如果学生不存在则返回null
     */
    @Override
    public StudentVO queryStudentInfo(Integer sid) {
        Student student = this.query().eq("StudentID", sid).one();
        if (student == null) return null;

        StudentBed studentBed1 = studentBedService.query().eq("StudentID", sid).one();
        Dormitory dormitory = dormitoryService.query().eq("DormitoryID", studentBed1.getDormitory_id()).one();
        StudentBed studentBed2 = studentBedService.query().eq("DormitoryID", studentBed1.getDormitory_id()).one();

        StudentVO vo = new StudentVO();
        vo.setStudentName(student.getName());
        vo.setDormitoryNumber(dormitory.getDormitory_number());

        return vo;
    }
    @Override
    public String addStudent(Student student){
        try {
            // 参数校验
            if(student == null) return "学生信息不能为空";

            // 插入学生信息
            int insertCount = stuMapper.insert(student);

            if (insertCount > 0) {
                return null; // 表示添加成功
            } else {
                return "添加学生信息失败";
            }
        } catch (Exception e) {
            // 异常处理
            return "添加学生信息发生异常：" + e.getMessage();
        }
    }

    @Transactional
    public String deleteStudent(Integer student_id) {
        try {
            // 参数校验
            if(stuMapper.selectById(student_id) == null) return "学生不存在";

            // 插入学生信息
            int deleteCount = stuMapper.deleteById(student_id);

            if (deleteCount > 0) {
                return null; // 表示添加成功
            } else {
                return "删除学生信息失败";
            }
        } catch (Exception e) {
            // 异常处理
            return "删除学生信息发生异常：" + e.getMessage();
        }
    }

    public String updateStudent(Student student) {
        try {

            Integer id = student.getStudent_id();

            // 参数校验
            if(stuMapper.selectById(id) == null) return "学生不存在";

            // 插入学生信息
            int updateCount = stuMapper.updateById(student);

            if (updateCount > 0) {
                return null; // 表示添加成功
            } else {
                return "删除学生信息失败";
            }
        } catch (Exception e) {
            // 异常处理
            return "删除学生信息发生异常：" + e.getMessage();
        }
    }
}

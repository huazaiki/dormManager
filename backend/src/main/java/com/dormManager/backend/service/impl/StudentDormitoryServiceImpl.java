package com.dormManager.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormManager.backend.entity.dto.Dormitory;
import com.dormManager.backend.entity.dto.Student;
import com.dormManager.backend.entity.dto.StudentBed;
import com.dormManager.backend.entity.vo.response.StudentVO;
import com.dormManager.backend.mapper.DormitoryMapper;
import com.dormManager.backend.mapper.StudentDormitoryMapper;
import com.dormManager.backend.mapper.StudentMapper;
import com.dormManager.backend.service.StudentDormitoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class StudentDormitoryServiceImpl extends ServiceImpl<StudentDormitoryMapper, StudentBed> implements StudentDormitoryService {

    @Resource
    StudentDormitoryMapper stuBedMapper;

    @Resource
    DormitoryMapper dormMapper;

    @Resource
    StudentMapper stuMapper;


    @Transactional
    @Override
    public String assignDormitory(String student_number, String dormitory_number) {
        Student student = stuMapper.selectOne(new QueryWrapper<Student>().eq("student_number", student_number));
        Dormitory dormitory = dormMapper.selectOne(new QueryWrapper<Dormitory>().eq("dormitory_number", dormitory_number));

        StudentBed studentBed = new StudentBed(null, student.getStudent_id(), dormitory.getDormitory_id());
        stuBedMapper.insert(studentBed);
        return null;
    }

    @Override
    public List<StudentVO> getDormitoryInfo(String building_number, String dormitory_number) {
        QueryWrapper<Dormitory> wrapper = new QueryWrapper<>();

        wrapper.eq("building_number", building_number);
        wrapper.eq("dormitory_number", dormitory_number);

        Integer dormitory_id = dormMapper.selectOne(wrapper).getDormitory_id();

        List<StudentBed> studentBedList = stuBedMapper.selectList(new QueryWrapper<StudentBed>().eq("dormitory_id", dormitory_id));

        List<StudentVO> studentVOS = new LinkedList<>();
        for (StudentBed sb :
                studentBedList) {
            StudentVO vo = new StudentVO();
            vo.setStudentName(stuMapper.selectById(sb.getStudent_id()).getName());
            vo.setDormitoryNumber(dormitory_number);
            studentVOS.add(vo);
        }

        return studentVOS;
    }
}

package com.dormManager.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormManager.backend.entity.dto.Dormitory;
import com.dormManager.backend.entity.dto.Student;
import com.dormManager.backend.entity.dto.StudentBed;
import com.dormManager.backend.entity.vo.response.StudentVO;
import com.dormManager.backend.mapper.DormitoryMapper;
import com.dormManager.backend.mapper.StudentBedMapper;
import com.dormManager.backend.mapper.StudentMapper;
import com.dormManager.backend.service.StudentBedService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class StudentBedServiceImpl extends ServiceImpl<StudentBedMapper, StudentBed> implements StudentBedService {

    @Resource
    StudentBedMapper stuBedMapper;

    @Resource
    DormitoryMapper dormMapper;

    @Resource
    StudentMapper stuMapper;


    @Override
    public String assignDormitory(Integer student_id, Integer dormitory_id) {
        if (dormitory_id == null)
            return "宿舍不存在，请重新分配";
        Dormitory dormitory = dormMapper.selectById(dormitory_id);
        if (dormitory.getOccupied_beds() >= dormitory.getTotal_beds())
            return "宿舍无空位，请重新选择";
        StudentBed studentBed = new StudentBed(null, student_id, dormitory_id);
        stuBedMapper.insert(studentBed);
        return null;
    }

    @Override
    public List<StudentVO> getDormitoryInfo(Integer dormitory_id) {
        List<StudentBed> studentBedList = stuBedMapper.selectList(new QueryWrapper<StudentBed>().eq("dormitory_id", dormitory_id));

        List<StudentVO> studentVOS = new LinkedList<>();
        for (StudentBed sb :
                studentBedList) {
            Student student = stuMapper.selectById(sb.getStudent_id());
            StudentVO vo = new StudentVO();
            vo.setStudentName(student.getName());
            vo.setDormitoryNumber(dormMapper.selectById(dormitory_id).getDormitory_number());
            studentVOS.add(vo);
        }
        return studentVOS;
    }
}

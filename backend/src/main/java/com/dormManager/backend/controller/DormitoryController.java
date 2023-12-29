package com.dormManager.backend.controller;

import com.dormManager.backend.entity.RestBean;
import com.dormManager.backend.entity.dto.Dormitory;
import com.dormManager.backend.entity.vo.response.StudentVO;
import com.dormManager.backend.service.DormitoryService;
import com.dormManager.backend.service.StudentDormitoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dorm")
public class DormitoryController {

    @Resource
    DormitoryService dormService;

    @Resource
    StudentDormitoryService studentDormitoryService;

    @GetMapping("/getAllDormitories")
    public RestBean<List<Dormitory>> getAllDormitories() {
        List<Dormitory> allDormitories = dormService.getAllDormitories();
        return RestBean.success(allDormitories);
    }

    @PostMapping("/addDormitory")
    public RestBean<Void> addDormitory(@RequestBody Dormitory dormitory){
        String msg = dormService.addDormitory(dormitory);
        return msg == null ? RestBean.success() : RestBean.failure(401, msg);
    }

    @PostMapping("/updateDormitory")
    public RestBean<Void> updateDormitory(@RequestBody Dormitory dormitory){
        String msg = dormService.updateDormitory(dormitory);
        return msg == null ? RestBean.success() : RestBean.failure(401, msg);
    }

    @DeleteMapping("/deleteDormitory/{dormitory_id}")
    public RestBean<Void> deleteDormitory(@PathVariable Integer dormitory_id){
        String msg = dormService.deleteDormitory(dormitory_id);
        return msg == null ? RestBean.success() : RestBean.failure(401, msg);
    }

    @PostMapping("/updateDormitoryByBuildingNumber")
    public RestBean<Void> updateDormitoryByBuildingNumber(@RequestParam String old_building_number,
                                                          @RequestParam String new_building_number){
        String msg = dormService.updateDormitoryByBuildingNumber(old_building_number, new_building_number);
        return msg == null ? RestBean.success() : RestBean.failure(401, msg);
    }

    @DeleteMapping("/deleteDormitoryByBuildingNumber/{building_number}")
    public RestBean<Void> deleteDormitoryByBuildingNumber(@PathVariable String building_number){
        String msg = dormService.deleteDormitoryByBuildingNumber(building_number);
        return msg == null ? RestBean.success() : RestBean.failure(401, msg);
    }

    @GetMapping("/getAllDormitoriesByBuildingNumber")
    public RestBean<List<Dormitory>> getAllDormitoriesByBuildingNumber(@RequestParam String building_number) {
        List<Dormitory> allDormitoriesByBuildingNumber = dormService.getAllDormitoriesByBuildingNumber(building_number);
        return RestBean.success(allDormitoriesByBuildingNumber);
    }

    @GetMapping("/getDormitoryInfo")
    public RestBean<List<StudentVO>> getDormitoryInfo(@RequestParam String building_number
                                                    , @RequestParam String dormitory_number){
        List<StudentVO> studentVOS= studentDormitoryService.getDormitoryInfo(building_number,dormitory_number);
        return RestBean.success(studentVOS);
    }
}

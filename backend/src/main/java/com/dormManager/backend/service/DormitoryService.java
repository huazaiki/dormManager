package com.dormManager.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dormManager.backend.entity.dto.Dormitory;

import java.util.List;

public interface DormitoryService extends IService<Dormitory> {

    public String addDormitory(Dormitory dormitory);

    public String updateDormitory(Dormitory dormitory);

    public String updateDormitoryByBuildingNumber(String building_number);

    public String deleteDormitory(Integer dormitory_id);

    public String deleteDormitoryByBuildingNumber(String building_number);

    public List<Dormitory> getAllDormitories();

    public List<Dormitory> getAllDormitoriesByBuildingNumber(String building_number);
}

package com.dormManager.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dormManager.backend.entity.dto.Dormitory;
import com.dormManager.backend.mapper.DormitoryMapper;
import com.dormManager.backend.service.DormitoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements DormitoryService {

    @Resource
    DormitoryMapper dormMapper;

    public String addDormitory(Dormitory dormitory) {
        if (dormitory == null) return "宿舍不存在";
        dormMapper.insert(dormitory);
        return null;
    }

    public String updateDormitory(Dormitory dormitory) {
        if (dormitory == null) return "宿舍不存在";
        dormMapper.updateById(dormitory);
        return null;
    }

    public String updateDormitoryByBuildingNumber(String building_number) {
        if (building_number == null) return "宿舍不存在";
        dormMapper.update(new QueryWrapper<Dormitory>().eq("building_number", building_number));
        return null;
    }

    @Transactional
    public String deleteDormitory(Integer dormitory_id) {
        if (dormMapper.selectById(dormitory_id) == null) return "宿舍不存在，请重新查询删除";
        dormMapper.deleteById(dormitory_id);
        return null;
    }

    @Transactional
    public String deleteDormitoryByBuildingNumber(String building_number) {
        List<Dormitory> dormitoryList = dormMapper.selectList(new QueryWrapper<Dormitory>().eq("building_number", building_number));
        if (dormitoryList.isEmpty()) return "宿舍不存在，请重新查询删除";
        dormMapper.delete(new QueryWrapper<Dormitory>().eq("building_number", building_number));
        return null;
    }


    @Override
    public List<Dormitory> getAllDormitoriesByBuildingNumber(String building_number) {

        // 参数校验
        if (building_number == null || building_number.isEmpty()) {
            return List.of();
        }

        try {
            // 查询整栋楼宿舍信息
            List<Dormitory> dormitoryList =
                    dormMapper.selectList(new QueryWrapper<Dormitory>().eq("building_number", building_number));

            return dormitoryList;
        } catch (Exception e) {
            // 异常处理
            throw new RuntimeException("检索宿舍时出错.", e);
        }
    }

    @Override
    public List<Dormitory> getAllDormitories() {
        return dormMapper.selectList(null);
    }
}

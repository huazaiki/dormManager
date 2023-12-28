package com.dormManager.backend.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("db_dormitories")
@AllArgsConstructor
public class Dormitory {

    @TableId(type = IdType.INPUT)
    Integer DormitoryID;

    String BuildingNumber;
    String DormitoryNumber;
    String Capacity;
    String OccupiedBeds;
    String TotalBeds;
}

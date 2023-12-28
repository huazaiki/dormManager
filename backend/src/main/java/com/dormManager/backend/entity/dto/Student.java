package com.dormManager.backend.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("db_students")
@AllArgsConstructor
public class Student {

    @TableId(type = IdType.INPUT)
    Integer StudentID;

    String Name;
    String StudentNumber;
    String Gender;
    String Email;
    Integer OccupancyStatus;

}

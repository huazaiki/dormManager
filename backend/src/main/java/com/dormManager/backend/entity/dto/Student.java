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

    @TableId("student_id")
    Integer student_id;

    String name;
    String student_number;
    String gender;
    String email;
    Integer occupancy_status;

}

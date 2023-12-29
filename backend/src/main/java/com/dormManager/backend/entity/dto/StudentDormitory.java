package com.dormManager.backend.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_dormitoryassignments")
@AllArgsConstructor
public class StudentDormitory {

    @TableId("assignment_id")
    Integer assignment_id;

    Integer student_id;
    Integer dormitory_id;
    Date assignment_date;
}

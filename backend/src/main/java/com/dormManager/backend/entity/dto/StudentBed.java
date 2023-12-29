package com.dormManager.backend.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("db_bedassignments")
@AllArgsConstructor
public class StudentBed {

    @TableId("assignment_id")
    Integer assignment_id;

    Integer student_id;
    Integer dormitory_id;
}

package com.dormManager.backend.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("db_bedassignments")
@AllArgsConstructor
public class StudentBed {

    @TableId
    Integer AssignmentID;

    Integer StudentID;
    Integer DormitoryID;
    Integer BedNumber;
}

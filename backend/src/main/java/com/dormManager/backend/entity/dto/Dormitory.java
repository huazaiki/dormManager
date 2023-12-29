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

    /**
     * 宿舍ID，唯一标识一个宿舍。
     */
    @TableId(type = IdType.INPUT)
    Integer dormitory_id;

    /**
     * 楼栋号，表示宿舍所在楼栋的编号。
     */
    String building_number;

    /**
     * 宿舍号，表示具体宿舍的编号。
     */
    String dormitory_number;

    /**
     * 宿舍容量，表示宿舍可以容纳的学生床位总数。
     */
    Integer capacity;

    /**
     * 已占用床位数，表示当前已经有多少床位被学生占用。
     */
    Integer occupied_beds;

    /**
     * 总床位数，表示宿舍总共有多少床位。
     */
    Integer total_beds;
}

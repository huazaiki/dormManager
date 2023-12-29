package com.dormManager.backend.entity.vo.response;

import lombok.Data;

import java.util.List;

/**
 * 通过宿舍号查询宿舍所有信息
 */
@Data
public class DormitoryVO {

    /**
     * 返回这个宿舍的所有学生信息
     */
    List<StudentVO> studentVOS;

}

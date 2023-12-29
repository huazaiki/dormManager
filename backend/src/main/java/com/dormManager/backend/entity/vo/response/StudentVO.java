package com.dormManager.backend.entity.vo.response;

import lombok.Data;

/**
 * 用于通过学号查询学生宿舍信息的 ResponseVO
 */

@Data
public class StudentVO {

    /**
     * 学生姓名
     */
    String studentName;

    /**
     * 宿舍号
     */
    String dormitoryNumber;

}

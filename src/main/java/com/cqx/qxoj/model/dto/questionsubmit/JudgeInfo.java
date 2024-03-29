package com.cqx.qxoj.model.dto.questionsubmit;

import lombok.Data;

/**
 * 题目提交程序执行信息
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息 enum
     */
    private String message;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间
     */
    private Long time;
}

package com.cqx.qxoj.model.dto.questionsubmit;

import lombok.Data;

/**
 * @author xqc
 * @version 1.0
 * @date 2024/3/7 0:02
 */
@Data
public class JudgeInfo {
    /**
     * 程序执行信息
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

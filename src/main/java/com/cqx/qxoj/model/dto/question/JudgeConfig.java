package com.cqx.qxoj.model.dto.question;

import lombok.Data;

/**
 * @author xqc
 * @version 1.0
 * @date 2024/3/6 23:59
 */
@Data
public class JudgeConfig {
    /**
     * 时间限制(ms)
     */
    private Long timeLimit;
    /**
     * 内存限制
     */
    private Long memoryLimit;
    /**
     * 堆栈限制
     */
    private Long stackLimit;

}

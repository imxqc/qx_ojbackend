package com.cqx.qxoj.judge.codesandbox.model;

import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行响应参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteResponse {
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}

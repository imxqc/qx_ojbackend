package com.cqx.qxoj.judge.strategy;

import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;
import com.cqx.qxoj.model.entity.Question;
import com.cqx.qxoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 传递到策略的参数
 */
@Data
public class JudgeContext {
    /**
     * 沙箱输出信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 题目用例输出list
     */
    private List<String> configOutputList;

    /**
     * 沙箱输出list
     */
    private List<String> outputList;

    /**
     * 对应的题目信息
     */
    private Question question;
    /**
     * 对应的题目提交信息
     */
    private QuestionSubmit questionSubmit;
}

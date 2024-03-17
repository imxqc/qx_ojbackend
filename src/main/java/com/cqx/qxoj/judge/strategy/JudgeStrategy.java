package com.cqx.qxoj.judge.strategy;

import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;

/**
 * @author xqc
 * @version 1.0
 * @date 2024/3/10 0:33
 * 不同情况(比如语言不同可能时间限制不同)会采用不同策略,所以定义策略接口,根据情况选择对应策略
 */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext context);
}

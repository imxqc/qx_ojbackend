package com.cqx.qxoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.cqx.qxoj.model.dto.question.JudgeConfig;
import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;
import com.cqx.qxoj.model.entity.Question;
import com.cqx.qxoj.model.enums.JudgeInfoMessageEnum;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * 默认策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext context) {
        JudgeInfo judgeInfo = context.getJudgeInfo();
        List<String> configOutputList = context.getConfigOutputList();
        List<String> outputList = context.getOutputList();
        Question question = context.getQuestion();
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        //如果memory不存在则赋值0
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        String judgeConfigStr = question.getJudgeConfig();
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResp = new JudgeInfo();
        judgeInfoResp.setMemory(memory);
        judgeInfoResp.setTime(time);


        //  判断沙箱输出和输入数量是否一致
        if (configOutputList.size() != outputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResp.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResp;
        }
        //沙箱输出和题目用例输出是否一致
        for (int i = 0; i < outputList.size(); i++) {
            if (!configOutputList.get(i).equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResp.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResp;
            }
        }
        //设置判断后的状态

        JudgeConfig Config = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long timeLimit = Config.getTimeLimit();
        Long memoryLimit = Config.getMemoryLimit();

        //判断内存是否超出
        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResp.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResp;
        }
        //判断时间是否超出
        if (time > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResp.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResp;
        }
        return judgeInfoResp;
    }
}

package com.cqx.qxoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.cqx.qxoj.model.dto.question.JudgeConfig;
import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;
import com.cqx.qxoj.model.entity.Question;
import com.cqx.qxoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * Java策略
 */
public class JavaJudgeStrategy implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext context) {
        //获取题目提交相关信息
        JudgeInfo judgeInfo = context.getJudgeInfo();
        List<String> configOutputList = context.getConfigOutputList();
        List<String> outputList = context.getOutputList();
        Question question = context.getQuestion();
        Long time = judgeInfo.getTime();
        Long memory = judgeInfo.getMemory();
        String judgeConfigStr = question.getJudgeConfig();

        //定义返回值judgeInfoResp
        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        JudgeInfo judgeInfoResp = new JudgeInfo();
        judgeInfoResp.setMemory(memory);
        judgeInfoResp.setTime(time);
        judgeInfoResp.setMessage(judgeInfoMessageEnum.getValue());


        //比对题目用例输出和沙箱输出的数量是否一致
        if (configOutputList.size() != outputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResp.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResp;
        }

        //依次比对题目用例输出和沙箱输出,判断沙箱输出和输入结果是否一致
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


        //判断内存是否超出 本地沙箱未实现内存逻辑 注释
        if (memory != null && memory > memoryLimit) {
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

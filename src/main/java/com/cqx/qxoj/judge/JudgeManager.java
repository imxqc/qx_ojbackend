package com.cqx.qxoj.judge;

import com.cqx.qxoj.judge.strategy.DefaultJudgeStrategy;
import com.cqx.qxoj.judge.strategy.JavaJudgeStrategy;
import com.cqx.qxoj.judge.strategy.JudgeContext;
import com.cqx.qxoj.judge.strategy.JudgeStrategy;
import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;
import com.cqx.qxoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 封装judgeserviceimp 选择语言逻辑都放到该类
 */
@Service
public class JudgeManager {
    public JudgeInfo doJudge(JudgeContext context) {
        QuestionSubmit questionSubmit = context.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy strategy = new DefaultJudgeStrategy();
        if (language.equals("java")) {
            strategy = new JavaJudgeStrategy();
        }
        return strategy.doJudge(context);
    }
}
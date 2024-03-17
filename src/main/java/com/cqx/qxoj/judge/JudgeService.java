package com.cqx.qxoj.judge;

import com.cqx.qxoj.model.entity.QuestionSubmit;

/**
 * judge service
 */
public interface JudgeService {
    public QuestionSubmit doJudge(long questionSubmitId);
}

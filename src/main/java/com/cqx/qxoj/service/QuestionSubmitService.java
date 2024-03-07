package com.cqx.qxoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqx.qxoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.cqx.qxoj.model.entity.QuestionSubmit;
import com.cqx.qxoj.model.entity.User;

/**
 * @author cqx
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2024-03-06 21:58:29
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 点赞
     *
     * @param questionSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);


}

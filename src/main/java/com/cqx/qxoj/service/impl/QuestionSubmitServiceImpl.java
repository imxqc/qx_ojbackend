package com.cqx.qxoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqx.qxoj.common.ErrorCode;
import com.cqx.qxoj.exception.BusinessException;
import com.cqx.qxoj.mapper.QuestionSubmitMapper;
import com.cqx.qxoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.cqx.qxoj.model.entity.Question;
import com.cqx.qxoj.model.entity.QuestionSubmit;
import com.cqx.qxoj.model.entity.QuestionSubmit;
import com.cqx.qxoj.model.entity.User;
import com.cqx.qxoj.model.enums.QuestionSubmitLanguageEnum;
import com.cqx.qxoj.model.enums.QuestionSubmitStatusEnum;
import com.cqx.qxoj.service.QuestionService;
import com.cqx.qxoj.service.QuestionSubmitService;
import com.cqx.qxoj.service.QuestionSubmitService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author cqx
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-03-06 21:58:29
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //todo 校验语言是否合理
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum value = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (value == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "编程语言错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        // TODO 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据输入失败");
        }
        return questionSubmit.getId();
    }


}





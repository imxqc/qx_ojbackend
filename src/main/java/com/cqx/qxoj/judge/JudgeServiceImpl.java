package com.cqx.qxoj.judge;

import cn.hutool.json.JSONUtil;
import com.cqx.qxoj.common.ErrorCode;
import com.cqx.qxoj.exception.BusinessException;
import com.cqx.qxoj.judge.codesandbox.CodeSandboxProxy;
import com.cqx.qxoj.judge.codesandbox.CodeSandbox;
import com.cqx.qxoj.judge.codesandbox.CodeSandboxFactory;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteRequest;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteResponse;
import com.cqx.qxoj.judge.strategy.DefaultJudgeStrategy;
import com.cqx.qxoj.judge.strategy.JudgeContext;
import com.cqx.qxoj.model.dto.question.JudgeCase;
import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;
import com.cqx.qxoj.model.entity.Question;
import com.cqx.qxoj.model.entity.QuestionSubmit;
import com.cqx.qxoj.model.enums.QuestionSubmitStatusEnum;
import com.cqx.qxoj.service.QuestionService;
import com.cqx.qxoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * jugdeservieimpl
 */
@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private JudgeManager judgeManager;


    public QuestionSubmit doJudge(long questionSubmitId) {
        //根据提交题目id获取题目提交信息 题目信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        Integer status = questionSubmit.getStatus();
        Long questionId = questionSubmit.getQuestionId();

        //判断提交信息是否为空
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交信息不存在");
        }

        //获取对应题目用例,题目限制等信息
        Question question = questionService.getById(questionId);
        String judgeCaseStr = question.getJudgeCase();
        String judgeConfigStr = question.getJudgeConfig();

        //判断题目信息是否为空
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        // 判断题目状态 为等待中则继续,不为等待中说明已经提交,这步可以视为上锁操作
        if (!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交状态错误");
        }

        //更新状态为判题中 更新数据库的数据 为该条提交数据上锁
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        //判断更新是否为空
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新失败");
        }

        //获取沙箱示例,并创建代理示例,构造方法中传入沙箱示例
        CodeSandbox sandbox = CodeSandboxFactory.newInstance(type);
        sandbox = new CodeSandboxProxy(sandbox);

        // 利用json工具将judgeCase判题用例字符串转为list,再利用stream获取inputList和outputList列表
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        List<String> outputList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        ExecuteRequest request = ExecuteRequest.builder().
                code(code)
                .inputList(inputList)
                .language(language)
                .build();

        //调用沙箱执行代码,获取执行结果
        ExecuteResponse executeResponse = sandbox.executeCode(request);

        //获取沙箱执行结果输出sandboxOutputList,判题信息sandboxJudgeInfo
        List<String> sandboxOutputList = executeResponse.getOutputList();
        //docker去除换行符
        for (int i = 0; i < sandboxOutputList.size(); i++) {
            String str = sandboxOutputList.get(i);

            if (executeResponse.getJudgeInfo().getMemory() != null) {
                String[] split = str.split("\\n");
                sandboxOutputList.set(i, split[0]);
            }
        }

        JudgeInfo sandboxJudgeInfo = executeResponse.getJudgeInfo();

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(sandboxJudgeInfo);
        judgeContext.setConfigOutputList(outputList);
        judgeContext.setOutputList(sandboxOutputList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        //如果沙箱的message不为ACCEPT,则沙箱未能正确执行代码,更新沙箱judgeInfo状态,无需比对输入输出,直接返回
        if (executeResponse.getStatus() != QuestionSubmitStatusEnum.SUCCEED.getValue()) {
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(sandboxJudgeInfo));
            update = questionSubmitService.updateById(questionSubmit);
            if (!update) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
            }
            return questionSubmit;
        }

        //策略模式校验程序输出和答案用例输出
        DefaultJudgeStrategy strategy = new DefaultJudgeStrategy();
        //获取沙箱判题状态
        JudgeInfo info = judgeManager.doJudge(judgeContext);
        //再次更新状态
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(info));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        QuestionSubmit newestSubmit = questionSubmitService.getById(questionSubmitId);
        return newestSubmit;
    }
}

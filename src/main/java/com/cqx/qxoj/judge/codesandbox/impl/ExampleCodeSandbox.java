package com.cqx.qxoj.judge.codesandbox.impl;

import com.cqx.qxoj.judge.codesandbox.CodeSandbox;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteRequest;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteResponse;
import com.cqx.qxoj.model.dto.questionsubmit.JudgeInfo;
import com.cqx.qxoj.model.enums.JudgeInfoMessageEnum;
import com.cqx.qxoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱 跑通业务流程
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteResponse executeCode(ExecuteRequest request) {
        List<String> inputList = request.getInputList();

        ExecuteResponse executeResponse = new ExecuteResponse();
        executeResponse.setOutputList(inputList);
        executeResponse.setMessage("测试执行中");
        executeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeResponse.setJudgeInfo(judgeInfo);

        return executeResponse;
    }

}

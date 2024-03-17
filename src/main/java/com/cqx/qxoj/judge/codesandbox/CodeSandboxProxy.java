package com.cqx.qxoj.judge.codesandbox;

import com.cqx.qxoj.judge.codesandbox.model.ExecuteRequest;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 沙箱代理
 */
@Slf4j
@AllArgsConstructor
public class CodeSandboxProxy implements CodeSandbox {

    private CodeSandbox sandbox;

    @Override
    public ExecuteResponse executeCode(ExecuteRequest request) {
        log.info("请求信息:{}" + request.toString());
        ExecuteResponse executeResponse = sandbox.executeCode(request);
        log.info("响应信息:{}" + executeResponse.toString());
        return executeResponse;
    }
}
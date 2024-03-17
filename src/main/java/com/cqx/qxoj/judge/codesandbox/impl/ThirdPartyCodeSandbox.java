package com.cqx.qxoj.judge.codesandbox.impl;

import com.cqx.qxoj.judge.codesandbox.CodeSandbox;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteRequest;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteResponse;

/**
 * 第三方代码沙箱 调用第三方沙箱接口l
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteResponse executeCode(ExecuteRequest request) {
        System.out.println("ThirdPartyCodeSandbox.executeCode");
        return null;
    }
}

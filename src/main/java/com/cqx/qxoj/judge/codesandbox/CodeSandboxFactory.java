package com.cqx.qxoj.judge.codesandbox;

import com.cqx.qxoj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.cqx.qxoj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.cqx.qxoj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 沙箱工厂
 */
public class CodeSandboxFactory {
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdparty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}

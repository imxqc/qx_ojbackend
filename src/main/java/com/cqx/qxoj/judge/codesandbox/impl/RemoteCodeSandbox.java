package com.cqx.qxoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cqx.qxoj.common.ErrorCode;
import com.cqx.qxoj.exception.BusinessException;
import com.cqx.qxoj.judge.codesandbox.CodeSandbox;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteRequest;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱 实际调用接口的沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {
    public static final String AUTH_REQUEST_HEADER = "auth";

    public static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteResponse executeCode(ExecuteRequest request) {
        System.out.println("远程代码沙箱");

//        String url = "http://localhost:8101/executeCode";
        String url = "http://192.168.149.100:8101/executeCode";
        String json = JSONUtil.toJsonStr(request);
        String respStr = HttpUtil.createPost(url)
                .body(json)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .execute()
                .body();
        if (StringUtils.isBlank(respStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "执行沙箱失败,错误信息" + respStr);
        }
        return JSONUtil.toBean(respStr, ExecuteResponse.class);
    }
}

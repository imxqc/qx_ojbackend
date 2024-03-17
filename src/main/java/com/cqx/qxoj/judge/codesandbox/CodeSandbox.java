package com.cqx.qxoj.judge.codesandbox;

import com.cqx.qxoj.judge.codesandbox.model.ExecuteRequest;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 沙箱接口
 */

public interface CodeSandbox {
    ExecuteResponse executeCode(ExecuteRequest request);
}


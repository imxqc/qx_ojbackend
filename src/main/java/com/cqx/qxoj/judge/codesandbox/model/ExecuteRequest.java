package com.cqx.qxoj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteRequest {
    private String language;

    private String code;

    private List<String> inputList;
}

package com.cqx.qxoj.judge.codesandbox;

import com.cqx.qxoj.judge.codesandbox.model.ExecuteRequest;
import com.cqx.qxoj.judge.codesandbox.model.ExecuteResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


/**
 * @author xqc
 * @version 1.0
 */
@SpringBootTest
class CodeSandboxTest {
    @Value("${codesandbox.type:example}")
    private String type;


    @Test
    public void test() {
        CodeSandbox sandbox = CodeSandboxFactory.newInstance(type);
        String code = "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        int a = Integer.parseInt(args[0]);\n" +
                "        int b = Integer.parseInt(args[1]);\n" +
                "        System.out.println(\"a+b=\" + (a + b));\n" +
                "    }\n" +
                "}\n";
        String language = "java";
        List<String> list = Arrays.asList("1 2", "3 4");

        ExecuteRequest request = ExecuteRequest.builder().
                code(code)
                .inputList(list)
                .language(language)
                .build();

        ExecuteResponse executeResponse = sandbox.executeCode(request);
        System.out.println("executeResponse = " + executeResponse);
    }

    @Test
    public void testByProxy() {
        CodeSandbox sandbox = CodeSandboxFactory.newInstance(type);
        sandbox = new CodeSandboxProxy(sandbox);
        String code = "string name";
        String language = "java";
        List<String> list = Arrays.asList("1 2", "3 4");

        ExecuteRequest request = ExecuteRequest.builder().
                code(code)
                .inputList(list)
                .language(language)
                .build();
        sandbox.executeCode(request);
    }
}
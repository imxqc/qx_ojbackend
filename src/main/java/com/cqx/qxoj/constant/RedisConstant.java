package com.cqx.qxoj.constant;

/**
 * Redis常量
 */
public interface RedisConstant {
    /**
     * 用户信息前缀
     */
    String USER_PREFIX = "qxoj:user";

    /**
     * 题目信息前缀
     */
    String QUESTION_PREFIX = "qxoj:question";

    /**
     * 题目信息前缀
     */
    String QUESTION_SUBMIT_PREFIX = "qxoj:questionSubmit";

    /**
     * 用户信息过期时间
     */
    int USER_EXPIRE_TIME = 3;

}

package com.cqx.qxoj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqx.qxoj.model.entity.SocialUser;
import com.cqx.qxoj.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 */
public interface OauthLoginService extends IService<User> {

    Boolean login(SocialUser socialUser, HttpServletRequest request);
}

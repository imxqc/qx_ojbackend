package com.cqx.qxoj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqx.qxoj.model.entity.SocialUser;
import com.cqx.qxoj.service.OauthLoginService;
import com.cqx.qxoj.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.cqx.qxoj.constant.OauthConstant.*;

/**
 * 用户接口
 */
@Controller
@RequestMapping("/oauth")
@Slf4j
public class OauthLoginController {
    @Autowired
    private OauthLoginService oauthLoginService;

    /**
     * gitee社交登录回调接口
     */
    @GetMapping("/gitee/success")
    public String oauthByGitee(@RequestParam("code") String code, HttpServletRequest request) throws Exception {
        //构建http请求
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", GRANT_TYPE);
        map.put("client_id", CLIENT_ID);
        map.put("code", code);
        map.put("client_secret", CLIENT_SECRET);
        map.put("redirect_uri", REDIRECT_URI);

        //获取access_token, expires_in
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post",
                new HashMap<>(), new HashMap<>(), map);

        //处理信息
        if (response.getStatusLine().getStatusCode() == 200) {
            //获取SocialUser信息
            String s = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(s, SocialUser.class);
            //从https://gitee.com/api/v5/user接口中获取id信息
            String uid = getUid(socialUser.getAccess_token());
            //uid与user类的unionId对应
            socialUser.setUid(uid);

            Boolean ok = oauthLoginService.login(socialUser, request);

            //注册成功 跳转首页
            if (ok) return "redirect:http://localhost:8080";

            //失败 重新登录
            return "redirect:http://localhost:8080/user/login";
        } else {
            //重定向到登录页
            return "redirect:http://localhost:8080/user/login";
        }

    }

    public String getUid(String token) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("access_token", token);

        HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get",
                new HashMap<>(), map);

        if (response.getStatusLine().getStatusCode() == 200) {
            String s = EntityUtils.toString(response.getEntity());
            JSONObject object = JSON.parseObject(s);
            String id = object.getString("id");
            System.out.println("获取成功,uid = " + id);
            return id;
        } else {
            System.out.println("获取uid失败");
            return null;
        }
    }

}

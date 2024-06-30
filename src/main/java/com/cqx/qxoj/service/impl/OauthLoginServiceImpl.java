package com.cqx.qxoj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqx.qxoj.common.ErrorCode;
import com.cqx.qxoj.exception.BusinessException;
import com.cqx.qxoj.mapper.UserMapper;
import com.cqx.qxoj.model.entity.SocialUser;
import com.cqx.qxoj.model.entity.User;
import com.cqx.qxoj.model.enums.UserRoleEnum;
import com.cqx.qxoj.service.OauthLoginService;
import com.cqx.qxoj.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.cqx.qxoj.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class OauthLoginServiceImpl extends ServiceImpl<UserMapper, User> implements OauthLoginService {

    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "qxoj";


    @Override
    public Boolean login(SocialUser socialUser, HttpServletRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("unionId", socialUser.getUid());
        User user = this.getOne(queryWrapper);

        // 被封号，禁止登录
        if (user != null && UserRoleEnum.BAN.getValue().equals(user.getUserRole())) {
            return false;
        }

        if (user == null) {
            //通过gitee获取名字信息name,将name设置为userAccount和userName, 通过gitee登录无需再设置userPassword
            user = new User();
            try {
                Map<String, String> map = new HashMap<>();
                map.put("access_token", socialUser.getAccess_token());

                HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<>(), map);

                if (response.getStatusLine().getStatusCode() == 200) {
                    String s = EntityUtils.toString(response.getEntity());
                    JSONObject object = JSON.parseObject(s);
                    String name = object.getString("name");
                    user.setUserName(name);
                    user.setUserAccount(name);
                    //默认用户名为密码
                    String encryptPassword = DigestUtils.md5DigestAsHex((SALT + name).getBytes());
                    user.setUserPassword(encryptPassword);
                } else {
                    System.out.println("接口获取信息失败");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            user.setUnionId(socialUser.getUid());


            boolean result = this.save(user);
            if (!result) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "登录失败");
            }
        }

        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        return true;
    }
}

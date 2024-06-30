package com.cqx.qxoj.model.entity;

import lombok.Data;

/**
 * 社交登录返回的 access_token 和 expires_in
 * @author xqc
 * @version 1.0
 * @date 2024/5/5 20:18
 */
@Data
public class SocialUser {
    //访问令牌
    private String access_token;
    private String token_type;
    //令牌时间
    private long expires_in;
    private String refresh_token;
    private String scope;
    private long created_at;
    //唯一uid
    private String uid;
}

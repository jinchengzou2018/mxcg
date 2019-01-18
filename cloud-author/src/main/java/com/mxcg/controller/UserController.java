package com.mxcg.controller;

import com.mxcg.common.web.entity.RetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/30 10:37
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@RestController
public class UserController {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


    @GetMapping("/user")
    public Principal user(Principal user) {
        System.out.println("Principal is complete.");
        return user;

    }








    /**
     * 注销登陆
     */
    @PostMapping("/removeToken")
    public RetEntity removeToken(String accessToken, String refreshToken) {
        Assert.hasText(accessToken, "accessToken is null");
        Assert.hasText(refreshToken, "refreshToken is null");

        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.removeAccessToken(accessToken);
        redisTokenStore.removeRefreshToken(refreshToken);

        return RetEntity.ok();
    }
}

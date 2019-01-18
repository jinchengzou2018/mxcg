package com.mxcg.controller;

import com.mxcg.common.util.TokenKit;
import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.entity.User;
import com.mxcg.model.UserInfo;
import com.mxcg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:09
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public RetEntity<UserInfo> info(HttpServletRequest request) {
        return RetEntity.ok().setBody(
                userService.getInfo(TokenKit.getUsername(request))
        );
    }

    @PutMapping("/updatePwd")
    public RetEntity updatePwd(HttpServletRequest request,String oldPassword,String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String username = TokenKit.getUsername(request);

        User user = userService.findByUsername(username);

        if(!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return RetEntity.error("原密码错误。");
        }
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        boolean flag = user.updateById();
        return flag ? RetEntity.ok() : RetEntity.error();
    }

    @GetMapping("/findByUsername/{username}")
    public RetEntity<User> findByUsername(@PathVariable String username) {
        try {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println(username);
            return RetEntity.ok().setBody(userService.findByUsername(username));
        } catch (Exception e) {
            e.printStackTrace();
            return RetEntity.error(e.getMessage());
        }
    }
}

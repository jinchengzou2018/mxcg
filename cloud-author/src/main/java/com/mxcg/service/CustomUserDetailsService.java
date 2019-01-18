package com.mxcg.service;

import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.entity.User;
import com.mxcg.feign.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/30 23:27
 * @description：${description}
 * @modified By：
 * @version: $version$
 */


public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserFeignClient userFeignClient;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RetEntity<User> retEntity = userFeignClient.findByUsername(username);
        System.out.println("@@@@@@@@@@@@2loadUserByUsername22: " + retEntity.getMessage().toString());
        if (retEntity.isStatus()==false) {
            System.out.println("loadUserByUsername: " + retEntity.getMessage());
            throw new UsernameNotFoundException(retEntity.getMessage());
        }

            User  user=(User)retEntity.getBody();

            String pwd=user.getPassword();

             BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
             String finalPassword = "{bcrypt}"+bCryptPasswordEncoder.encode(pwd);

            System.out.println(finalPassword);
            user.setPassword(finalPassword);
            UserDetails manager= new CustomUserDetails(user);



            return    manager;
    }
}


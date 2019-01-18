package com.mxcg.service;


import com.google.common.collect.Lists;
import com.mxcg.common.util.CollectionUtil;
import com.mxcg.entity.Role;
import com.mxcg.entity.User;
import com.mxcg.entity.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/30 10:41
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

public class CustomUserDetails implements UserDetails, Serializable {

    private User userEntity;

    public CustomUserDetails(User userEntity) {
        this.userEntity = userEntity;


    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 构建角色集合: Role -> SimpleGrantedAuthority
        Set<Role> roles = userEntity.getRoleSet();
        List<GrantedAuthority> authorities = Lists.newArrayList();
        if (CollectionUtil.isNotEmpty(roles)) {
            roles.stream()
                    .map((role) -> new SimpleGrantedAuthority(role.getCode()))
                    .forEach((authority) -> authorities.add(authority));
            System.out.println("buildAuthority:" + authorities.toString());
            //log.debug("buildAuthority:" + authorities.toString());
        }
        return authorities;
    }

    @Override
    public String getPassword() {

        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 账户是否没有过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账户是否没有锁定
        return userEntity.getStatus() != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 凭证是否没有过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 账户是否启用
        return userEntity.getStatus() == UserStatus.ENABLED;
    }
}
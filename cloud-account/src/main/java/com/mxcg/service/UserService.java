package com.mxcg.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mxcg.Repository.ResourceMapper;
import com.mxcg.Repository.UserMapper;
import com.mxcg.api.UserServiceApi;
import com.mxcg.common.exception.CloudException;
import com.mxcg.common.util.CollectionUtil;
import com.mxcg.entity.Resource;
import com.mxcg.entity.User;
import com.mxcg.entity.enums.ResourceType;
import com.mxcg.model.UserInfo;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:52
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Service
public class UserService  extends ServiceImpl<UserMapper, User> implements UserServiceApi {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public User findByUsername(String username) throws CloudException {
        User user = userMapper.findByUsername(username);
        if(user == null) {
            throw new CloudException("找不到用户: " + username);
        }
        return user;
    }

    @Override
    public void deleteUserAssociatedRoles(Long userId) {
        userMapper.deleteUserAssociatedRoles(userId);
    }

    @Override
    public UserInfo getInfo(String username) {
        // 获取当前用户
        User user = findByUsername(username);
        // 用户资源（所有）
        List<Resource> resources = Optional.ofNullable(resourceMapper.findByUserId(user.getId())).orElse(Lists.newArrayList());
        return new UserInfo()
                .setUser(user)
                // 角色列表
                .setRoles(() -> user.getRoleSet().stream().map((role) -> role.getCode()).toArray(String[]::new))
                // 菜单
                .setMenus(() -> filterBy(resources,(res) -> StrUtil.isNotEmpty(res.getCode()) && res.getType() == ResourceType.MENU))
                // 权限列表（按钮）
                .setPermissions(() -> filterBy(resources, (res) -> StrUtil.isNotEmpty(res.getCode()) && res.getType() == ResourceType.BUTTON));
    }

    private String[] filterBy(List<Resource> resources, Predicate<Resource> predicate) {
        if(CollectionUtil.isEmpty(resources)) {
            return new String[0];
        }
        return resources.stream().filter(predicate).map((res) -> res.getCode()).toArray(String[]::new);
    }

}

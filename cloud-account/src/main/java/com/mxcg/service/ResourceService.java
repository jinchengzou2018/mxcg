package com.mxcg.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mxcg.Repository.ResourceMapper;
import com.mxcg.api.ResourceServiceApi;
import com.mxcg.entity.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:31
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Service
public class ResourceService  extends ServiceImpl<ResourceMapper, Resource> implements ResourceServiceApi {




    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> findByUserId(Long userId) {
        return Optional.ofNullable(resourceMapper.findByUserId(userId))
                .orElse(Lists.newArrayList());
    }

    @Override
    public List<Resource> findWithUrlNotNullByRoleCode(String roleCode) {

        System.out.println("###################");
        System.out.println("Role"+roleCode);
        return  Optional.ofNullable(resourceMapper.findWithUrlNotNullByRoleCode(roleCode))
                .orElse(Lists.newArrayList());
    }

    @Override
    public List<Resource> getAllMenuByUsername(String username) {
        return Optional.ofNullable(resourceMapper.getAllMenuByUsername(username))
                .orElse(Lists.newArrayList());
    }
}

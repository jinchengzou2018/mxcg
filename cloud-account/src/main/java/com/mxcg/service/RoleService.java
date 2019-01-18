package com.mxcg.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mxcg.Repository.RoleMapper;
import com.mxcg.api.RoleServiceApi;
import com.mxcg.entity.Role;
import org.springframework.stereotype.Service;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:53
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Service
public class RoleService  extends ServiceImpl<RoleMapper, Role> implements RoleServiceApi {
}

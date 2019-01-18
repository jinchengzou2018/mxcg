package com.mxcg.service.impl;

import com.mxcg.common.util.CollectionUtil;
import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.entity.Resource;
import com.mxcg.feign.ResourceFeignClient;
import com.mxcg.service.PermissionService;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/28 19:41
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private ResourceFeignClient resourceFeignClient;

    static AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        // 用户信息是否存在
        if (authentication.getPrincipal() != null) {
            // 权限合集 (角色)
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println(authentication.getPrincipal().toString());
            List<SimpleGrantedAuthority> authorities =
                    (List<SimpleGrantedAuthority>) authentication.getAuthorities();
            System.out.println(authorities.toString());
            if (CollectionUtil.isNotEmpty(authorities)) {
                for (SimpleGrantedAuthority authority : authorities) {
                    // 取当前角色关联的资源
                    RetEntity<List<Resource>> retEntity =
                            resourceFeignClient.findByRoleCode(authority.getAuthority());

                    if (retEntity.isStatus() == false && CollectionUtil.isNotEmpty(retEntity.getBody())) {
                        // 当前请求是否包含在菜单合集内 && 请求方式是否一致
                        boolean hasPermission = retEntity.getBody().stream()
                                .anyMatch((resource) -> {
                                    String url = resource.getUrl();
                                    String method = resource.getMethod() != null ? resource.getMethod().toString() : null;
                                    return StrUtil.isNotEmpty(url)
                                            && StrUtil.isNotEmpty(method)
                                            && pathMatcher.match(url, request.getRequestURI())
                                            && method.equalsIgnoreCase(request.getMethod());
                                });
                        if (hasPermission) return true;
                    }
                }

            }
                }
            return false;
        }

}

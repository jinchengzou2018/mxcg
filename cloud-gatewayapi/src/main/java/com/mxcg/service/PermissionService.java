package com.mxcg.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/28 19:51
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

public interface PermissionService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}

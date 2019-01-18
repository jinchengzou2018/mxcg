package com.mxcg.controller;

import com.mxcg.common.util.TokenKit;
import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.entity.Resource;
import com.mxcg.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:06
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping("/findByRoleCode/{roleCode}")
    public RetEntity<List<Resource>> findByRoleCode(@PathVariable("roleCode") String roleCode) {
      System.out.println("######################################################");
        System.out.println(roleCode);
        return RetEntity.ok().setBody(resourceService.findWithUrlNotNullByRoleCode(roleCode));
    }

    @RequestMapping("/findByUserId/{userId}")
    public RetEntity<List<Resource>> findByUserId(@PathVariable("userId") Long userId) {
        return RetEntity.ok().setBody(resourceService.findByUserId(userId));
    }

    @RequestMapping("/all")
    public RetEntity<List<Resource>> getAllMenuByUser(HttpServletRequest request) {
        String username = TokenKit.getUsername(request);
        return RetEntity.ok().setBody(resourceService.getAllMenuByUsername(username));
    }

    @RequestMapping("/allTest")
    public RetEntity<List<Resource>> allTest(String username) {
        return RetEntity.ok().setBody(resourceService.getAllMenuByUsername(username));
    }
}
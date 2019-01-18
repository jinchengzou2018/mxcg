package com.mxcg.controller;

import com.mxcg.common.web.entity.RetEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:08
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@RestController
@RequestMapping("/role")
public class RoleController {


    @PostMapping("/test")
    public RetEntity test(@RequestParam Map<String, String> parameters) {

        System.out.println(parameters);

        return RetEntity.ok();
    }
}

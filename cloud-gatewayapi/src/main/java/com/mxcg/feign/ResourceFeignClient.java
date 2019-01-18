package com.mxcg.feign;


import com.mxcg.common.Constant;
import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.entity.Resource;
import com.mxcg.feign.fallback.ResourceFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/28 19:04
 * @description：${description}
 * @modified By：
 * @version: $version$
 */


@FeignClient(name = Constant.Service.CLOUD_ACCOUNT, fallback = ResourceFeignClientFallback.class)
public interface ResourceFeignClient {



    @RequestMapping(value = "/resource/findByRoleCode/{roleCode}",consumes="application/json")
    RetEntity<List<Resource>> findByRoleCode(@PathVariable("roleCode") String roleCode);




}

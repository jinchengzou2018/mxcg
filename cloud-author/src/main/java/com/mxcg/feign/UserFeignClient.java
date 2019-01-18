

package com.mxcg.feign;


import com.mxcg.common.Constant;
import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.entity.User;
import com.mxcg.feign.fallback.UserFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**

 */
@FeignClient(name = Constant.Service.CLOUD_ACCOUNT, fallback = UserFeignClientFallback.class)
public interface UserFeignClient {


    @RequestMapping(value="/user/findByUsername/{username}",consumes="application/json")
    RetEntity<User> findByUsername(@PathVariable("username") String username);
}

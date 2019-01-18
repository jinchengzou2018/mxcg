
package com.mxcg.feign.fallback;


import com.mxcg.common.Constant;
import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.feign.UserFeignClient;
import org.springframework.stereotype.Component;

/**
 */

@Component
public class UserFeignClientFallback implements UserFeignClient {
    @Override
    public RetEntity findByUsername(String username) {

        int retCode = Constant.Code.SC_FEIGN_FALLBACK;
        String retMessage = String.format(
            "Feign调用接口失败(from: %s, Method: findByUsername, Params: %s)",
            Constant.Service.CLOUD_ACCOUNT, username
        );
        System.out.println("###############");
        System.out.println(retMessage.toString());
    //  log.error(retMessage);
        return RetEntity.error(retCode, retMessage);
    }
}

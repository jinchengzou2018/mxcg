package com.mxcg.feign.fallback;

import com.mxcg.common.Constant;
import com.mxcg.common.web.entity.RetEntity;
import com.mxcg.feign.ResourceFeignClient;
import org.springframework.stereotype.Component;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/28 19:04
 * @description：${description}
 * @modified By：
 * @version: $version$
 */




@Component
public class ResourceFeignClientFallback implements ResourceFeignClient {

    @Override
    public RetEntity findByRoleCode(String roleCode) {

        int retCode = Constant.Code.SC_FEIGN_FALLBACK;
        String retMessage = String.format("Feign调用接口失败(from: %s, Method: findByRoleCode, Params: %s)",
                Constant.Service.CLOUD_ACCOUNT, roleCode
        );

        return RetEntity.error(retCode, retMessage);
    }


}
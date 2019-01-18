package com.mxcg.fallback;

import com.mxcg.common.Constant;
import com.mxcg.common.web.entity.RetEntity;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.http.client.ClientHttpResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/28 18:58
 * @description：${description}
 * @modified By：
 * @version: $version$
 */


@Component
public class ZuulFallbackProvider implements FallbackProvider {

    @Override
    public String getRoute() {
        // service id
        return "*";
    }



    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause)
    {
        return getClientHttpResponse(null);
    }


    ClientHttpResponse getClientHttpResponse(Throwable throwable) {

        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() {
                return HttpStatus.OK.getReasonPhrase();
            }

            @Override
            public void close() {}

            @Override
            public InputStream getBody() {
                RetEntity retEntity = RetEntity.error(
                        Constant.Code.SC_FEIGN_FALLBACK,
                        Constant.Message.SC_FEIGN_FALLBACK
                );
                if(throwable != null && throwable.getMessage() != null) {
                    //retMessage += "[" + throwable.getMessage() + "]";
                    retEntity.setBody(throwable.getMessage());
                }
                return new ByteArrayInputStream(retEntity.toString().getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return httpHeaders;
            }
        };
    }
}

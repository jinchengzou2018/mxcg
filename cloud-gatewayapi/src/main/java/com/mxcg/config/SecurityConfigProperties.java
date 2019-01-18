package com.mxcg.config;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author ：zoujincheng
 * @date ：Created in 2018/12/28 18:55
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityConfigProperties {

    private List<String> excludeUrls = Lists.newArrayList();
}

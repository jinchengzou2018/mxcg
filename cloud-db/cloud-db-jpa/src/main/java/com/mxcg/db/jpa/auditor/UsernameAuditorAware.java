package com.mxcg.db.jpa.auditor;

import java.util.Optional;

import com.mxcg.core.security.WebContextUtil;
import org.springframework.data.domain.AuditorAware;


public class UsernameAuditorAware implements AuditorAware<Long>{

    @Override
    public Optional<Long> getCurrentAuditor() {
        Long currentUserName = WebContextUtil.getUserkey();
        return Optional.ofNullable(currentUserName);
    }
}

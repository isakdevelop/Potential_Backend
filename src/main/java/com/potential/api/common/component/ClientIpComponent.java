package com.potential.api.common.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ClientIpComponent {
    public String getClientIp() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getDetails();
    }
}
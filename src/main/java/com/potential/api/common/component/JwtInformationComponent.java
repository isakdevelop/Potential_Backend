package com.potential.api.common.component;

import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.model.User;
import com.potential.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtInformationComponent {
    private final UserRepository userRepository;

    public String getUserIdFromJWT() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public User certificationUserJWT(String jwt) {
        return userRepository.findById(getUserIdFromJWT())
                .orElseThrow(() -> new PotentialException(com.potential.api.common.enums.Error.FORBIDDEN.getStatus(), Error.FORBIDDEN.getMessage()));
    }

}
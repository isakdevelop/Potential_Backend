package com.potential.api.common.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_BUSINESS("ROLE_BUSINESS"),
    ;

    private final String role;
}

package com.potential.api.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthLoginRequestDto {
    private String role;
    private String token;
    private String name;
    private String userName;
}

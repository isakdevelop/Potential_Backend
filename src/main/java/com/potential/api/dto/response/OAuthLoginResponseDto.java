package com.potential.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthLoginResponseDto {
    private String role;
    private String token;
    private String name;
    private String userName;
}

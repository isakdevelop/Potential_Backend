package com.potential.api.dto.response;

import com.potential.api.dto.ResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LoginResponseDto extends ResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final int expirationTime;
    private final String role;
}

package com.potential.api.dto.response;

import com.potential.api.common.enums.Role;
import com.potential.api.dto.ResponseDto;
import lombok.Getter;

@Getter
public class LoginResponseDto extends ResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final int expirationTime;
    private final String role;

    public LoginResponseDto(int status, String message, String accessToken, String refreshToken, int expirationTime, Role role) {
        super(status, message);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
        this.role = String.valueOf(role);
    }
}

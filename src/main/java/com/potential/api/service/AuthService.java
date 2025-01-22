package com.potential.api.service;

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.potential.api.dto.request.LoginRequestDto;
import com.potential.api.dto.response.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    LoginResponseDto generateAccessToken(RefreshToken refreshToken);
}

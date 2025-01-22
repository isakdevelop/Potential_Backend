package com.potential.api.controller;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.LoginRequestDto;
import com.potential.api.dto.response.LoginResponseDto;
import com.potential.api.service.AuthService;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<LoginResponseDto> getToken(@RequestBody RefreshToken refreshToken) {
        return ResponseEntity.ok(authService.generateAccessToken(refreshToken));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

}
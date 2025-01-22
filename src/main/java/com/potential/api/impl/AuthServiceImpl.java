package com.potential.api.impl;

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.potential.api.common.component.JwtProvider;
import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.FisherException;
import com.potential.api.dto.request.LoginRequestDto;
import com.potential.api.dto.response.LoginResponseDto;
import com.potential.api.model.User;
import com.potential.api.repository.UserRepository;
import com.potential.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserName(loginRequestDto.getUserName())
                .orElseThrow(() -> new FisherException(Error.NOT_FOUND.getStatus(), Error.NOT_FOUND.getMessage()));

       if (matchesPassword(loginRequestDto.getPassword(), user.getPassword())) {
            String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());
            String refreshToken = jwtProvider.createRefreshToken(user.getId());

            return new LoginResponseDto(HttpStatus.OK.value(), "로그인에 성공하였습니다.", accessToken, refreshToken, 3600, user.getRole());
       } else {
           throw new FisherException(Error.UNAUTHORIZED.getStatus(), Error.UNAUTHORIZED.getMessage());
       }

    }

    @Override
    public LoginResponseDto generateAccessToken(RefreshToken refreshToken) {
        return null;
    }

    private Boolean matchesPassword(String userPassword, String password) {
        return passwordEncoder.matches(password, userPassword);
    }
}

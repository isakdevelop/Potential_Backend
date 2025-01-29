package com.potential.api.impl;

import com.nimbusds.oauth2.sdk.token.RefreshToken;
import com.potential.api.common.component.JwtProviderComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
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
    private final JwtProviderComponent jwtProviderComponent;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserName(loginRequestDto.getUserName())
                .orElseThrow(() -> new PotentialException(Error.NOT_FOUND.getStatus(), Error.NOT_FOUND.getMessage()));

       if (matchesPassword(loginRequestDto.getPassword(), user.getPassword())) {
            String accessToken = jwtProviderComponent.createAccessToken(user.getId(), user.getRole());
            String refreshToken = jwtProviderComponent.createRefreshToken(user.getId());

            return LoginResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("로그인에 성공하였습니다.")
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expirationTime(3600)
                    .role(String.valueOf(user.getRole()))
                    .build();
       } else {
           throw new PotentialException(Error.UNAUTHORIZED.getStatus(), Error.UNAUTHORIZED.getMessage());
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

package com.potential.api.impl;

import com.potential.api.auth.CustomOAuth2User;
import com.potential.api.auth.userInfo.KakaoUserInfo;
import com.potential.api.auth.userInfo.NaverUserInfo;
import com.potential.api.auth.userInfo.OAuth2UserInfo;
import com.potential.api.common.component.JwtProviderComponent;
import com.potential.api.common.enums.OAuthType;
import com.potential.api.common.enums.Role;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.request.OAuthLoginRequestDto;
import com.potential.api.model.User;
import com.potential.api.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final JwtProviderComponent jwtProviderComponent;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oauth2UserInfo;

        if (registrationId.equals("naver")) {
            oauth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            throw new PotentialException(HttpStatus.NOT_FOUND.value(), "지원하지 않는 소셜 로그인입니다.");
        }

        User oauthUser = userRepository.findByEmail(oauth2UserInfo.getEmail()).orElseGet(() -> {
            User user = User.builder()
                    .userName(oauth2UserInfo.getProvider() + "_" + UUID.randomUUID())
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .email(oauth2UserInfo.getEmail())
                    .name(oauth2UserInfo.getName())
                    .phone(oauth2UserInfo.getMobile())
                    .profilePath("") // Default로 빈 값 설정
                    .role(Role.ROLE_USER)
                    .oAuthType(getSignType(oauth2UserInfo.getProvider()))
                    .build();
            return userRepository.save(user);
        });

        String token = jwtProviderComponent.createAccessToken(oauthUser.getUserName(), oauthUser.getRole());

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("token", token);

        OAuthLoginRequestDto oAuthLoginRequestDto = OAuthLoginRequestDto.builder()
                .role("ROLE_USER")
                .name(oauth2UserInfo.getName())
                .userName(oauth2UserInfo.getProvider() + "_" + UUID.randomUUID())
                .token(token)
                .build();

        return new CustomOAuth2User(oAuthLoginRequestDto);
    }

    private OAuthType getSignType(String provider) {
        return switch (provider.toLowerCase()) {
            case "naver" -> OAuthType.Naver;
            case "kakao" -> OAuthType.Kakao;
            default -> throw new PotentialException(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 소셜 로그인입니다.");
        };
    }
}
package com.potential.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OAuthType {
    Naver("네이버"),
    Kakao("카카오")
    ;

    private final String oAuthType;
}

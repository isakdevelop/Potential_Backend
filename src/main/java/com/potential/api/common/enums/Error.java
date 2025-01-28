package com.potential.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum Error {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "인증 정보가 올바르지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "잘못된 형식입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "접근 권한이 존재하지않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 값 입니다."),
    CONFLICT(HttpStatus.CONFLICT.value(), "이미 중복된 값입니다."),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY.value(), "서버가 처리를 할 수 없는 데이터가 포함되었습니다.");
    ;

    private final int status;
    private final String message;
}

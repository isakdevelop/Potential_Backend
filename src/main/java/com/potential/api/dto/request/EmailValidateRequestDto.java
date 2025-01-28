package com.potential.api.dto.request;

import lombok.Getter;

@Getter
public class EmailValidateRequestDto {
    private String email;
    private String password;
}

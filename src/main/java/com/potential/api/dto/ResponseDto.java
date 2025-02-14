package com.potential.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public class ResponseDto {
    private int status;
    private String message;
}

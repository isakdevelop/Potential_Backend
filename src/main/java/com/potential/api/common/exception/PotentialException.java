package com.potential.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PotentialException extends RuntimeException{
    private int status;
    private String message;
}

package com.potential.api.common.exception;

import com.potential.api.dto.ResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PotentialExceptionHandler {
    @ExceptionHandler(PotentialException.class)
    public final ResponseEntity<ResponseDto> handleFisherException(PotentialException potentialException) {
        ResponseDto responseDto = new ResponseDto(
                potentialException.getStatus(),
                potentialException.getMessage()
        );
        return new ResponseEntity<>(responseDto, HttpStatusCode.valueOf(potentialException.getStatus()));
    }
}

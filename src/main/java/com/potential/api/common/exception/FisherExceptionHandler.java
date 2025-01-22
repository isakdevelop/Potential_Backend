package com.potential.api.common.exception;

import com.potential.api.dto.ResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FisherExceptionHandler {
    @ExceptionHandler(FisherException.class)
    public final ResponseEntity<ResponseDto> handleFisherException(FisherException fisherException) {
        ResponseDto responseDto = new ResponseDto(
                fisherException.getStatus(),
                fisherException.getMessage()
        );
        return new ResponseEntity<>(responseDto, HttpStatusCode.valueOf(fisherException.getStatus()));
    }
}

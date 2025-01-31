package com.potential.api.service;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.EmailRequestDto;
import com.potential.api.dto.request.EmailValidateRequestDto;

public interface EmailService {
    ResponseDto checkDuplicateEmail(EmailRequestDto emailRequestDto);

    ResponseDto emailAuthentication(EmailRequestDto emailRequestDto);

    ResponseDto validateEmail(EmailValidateRequestDto emailValidateRequestDto);

    ResponseDto changeUserEmail(EmailRequestDto emailRequestDto);
}

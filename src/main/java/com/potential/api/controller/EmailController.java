package com.potential.api.controller;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.EmailRequestDto;
import com.potential.api.dto.request.EmailValidateRequestDto;
import com.potential.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/checkDuplicateEmail")
    public ResponseEntity<ResponseDto> checkDuplicateEmail(@RequestBody EmailRequestDto emailRequestDto) {
        return ResponseEntity.ok(emailService.checkDuplicateEmail(emailRequestDto));
    }

    @PostMapping("/emailAuthentication")
    public ResponseEntity<ResponseDto> emailAuthentication(@RequestBody EmailRequestDto emailRequestDto) {
        return ResponseEntity.ok(emailService.emailAuthentication(emailRequestDto));
    }

    @PostMapping("/validateEmail")
    public ResponseEntity<ResponseDto> validateEmail(@RequestBody EmailValidateRequestDto emailValidateRequestDto) {
        return ResponseEntity.ok(emailService.validateEmail(emailValidateRequestDto));
    }

    @PatchMapping("/changeEmail")
    public ResponseEntity<ResponseDto> changeEmail(@RequestBody EmailRequestDto emailRequestDto) {
        return ResponseEntity.ok(emailService.changeUserEmail(emailRequestDto));
    }
}

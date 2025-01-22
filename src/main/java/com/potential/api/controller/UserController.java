package com.potential.api.controller;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserNameRequestDto;
import com.potential.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/checkDuplicateUserName")
    public ResponseEntity<ResponseDto> checkDuplicateUserName(@RequestBody UserNameRequestDto userNameRequestDto) {
        return ResponseEntity.ok(userService.checkDuplicateUserName(userNameRequestDto));
    }
}

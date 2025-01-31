package com.potential.api.controller;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserNameRequestDto;
import com.potential.api.service.UserService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/checkDuplicateUserName")
    public ResponseEntity<ResponseDto> checkDuplicateUserName(@RequestBody UserNameRequestDto userNameRequestDto) {
        return ResponseEntity.ok(userService.checkDuplicateUserName(userNameRequestDto));
    }

    @PatchMapping("/changeUserName")
    public ResponseEntity<ResponseDto> changeUserName(@RequestBody UserNameRequestDto userNameRequestDto) {
        return ResponseEntity.ok(userService.changeUserName(userNameRequestDto));
    }

    @PatchMapping("/changProfile")
    public ResponseEntity<ResponseDto> changeProfile(@RequestPart(value = "image", required = false) MultipartFile image)
                                                     throws IOException {
        return ResponseEntity.ok(userService.changeProfile(image));
    }
}

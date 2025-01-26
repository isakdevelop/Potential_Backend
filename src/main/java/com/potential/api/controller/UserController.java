package com.potential.api.controller;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserEmailRequestDto;
import com.potential.api.dto.request.UserNameRequestDto;
import com.potential.api.dto.request.UserReceiveEmailRequestDto;
import com.potential.api.service.UserService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/changeUserName")
    public ResponseEntity<ResponseDto> changeUserName(@RequestBody UserNameRequestDto userNameRequestDto) {
        return ResponseEntity.ok(userService.changeUserName(userNameRequestDto));
    }

    @PostMapping("/checkDuplicateEmail")
    public ResponseEntity<ResponseDto> checkDuplicateEmail(@RequestBody UserEmailRequestDto userEmailRequestDto) {
        return ResponseEntity.ok(userService.checkDuplicateEmail(userEmailRequestDto));
    }

    @PostMapping("/validateEmail")
    public ResponseEntity<ResponseDto> validateEmail(@RequestBody UserEmailRequestDto userEmailRequestDto) {
        return ResponseEntity.ok(userService.validateEmail(userEmailRequestDto));
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<ResponseDto> changeEmail(@RequestBody UserEmailRequestDto userEmailRequestDto) {
        return ResponseEntity.ok(userService.changeUserEmail(userEmailRequestDto));
    }

    @PostMapping("/receiveEmail")
    public ResponseEntity<ResponseDto> receiveEmail(@RequestBody UserReceiveEmailRequestDto userReceiveEmailDto) {
        return ResponseEntity.ok(userService.receiveEmail(userReceiveEmailDto));
    }

    @PostMapping("/changProfile")
    public ResponseEntity<ResponseDto> changeProfile(@RequestPart(value = "image", required = false) MultipartFile image)
                                                     throws IOException {
        return ResponseEntity.ok(userService.changeProfile(image));
    }
}

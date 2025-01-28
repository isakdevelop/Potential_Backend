package com.potential.api.service;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserNameRequestDto;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseDto checkDuplicateUserName(UserNameRequestDto userNameRequestDto);

    ResponseDto changeUserName(UserNameRequestDto userNameRequestDto);

    ResponseDto changeProfile(MultipartFile image) throws IOException;
}

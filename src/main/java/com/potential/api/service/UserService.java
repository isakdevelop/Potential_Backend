package com.potential.api.service;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserNameRequestDto;

public interface UserService {
    ResponseDto checkDuplicateUserName(UserNameRequestDto userNameRequestDto);
}

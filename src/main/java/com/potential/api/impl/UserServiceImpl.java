package com.potential.api.impl;

import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.FisherException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserNameRequestDto;
import com.potential.api.repository.UserRepository;
import com.potential.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public ResponseDto checkDuplicateUserName(UserNameRequestDto userNameRequestDto) {
        if (userRepository.existsByUserName(userNameRequestDto.getUserName())) {
            throw new FisherException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage());
        }

        return new ResponseDto(HttpStatus.OK.value(), "중복된 아이디가 존재하지 않습니다.");
    }

}

package com.potential.api.impl;

import com.potential.api.common.component.FileDeleteComponent;
import com.potential.api.common.component.ImageStorageComponent;
import com.potential.api.common.component.JwtInformationComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserNameRequestDto;
import com.potential.api.model.User;
import com.potential.api.repository.UserRepository;
import com.potential.api.service.UserService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtInformationComponent jwtInformationComponent;
    private final ImageStorageComponent imageStorageComponent;
    private final FileDeleteComponent fileDeleteComponent;

    @Override
    public ResponseDto checkDuplicateUserName(UserNameRequestDto userNameRequestDto) {
        if (userRepository.existsByUserName(userNameRequestDto.getUserName())) {
            throw new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage());
        }

        return new ResponseDto(HttpStatus.OK.value(), "사용 가능한 아이디입니다.");
    }

    @Transactional
    @Override
    public ResponseDto changeUserName(UserNameRequestDto userNameRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());
        user.changeUserName(userNameRequestDto.getUserName());

        userRepository.save(user);

        return new ResponseDto(HttpStatus.OK.value(), "닉네임 변경이 완료되었습니다.");
    }

    @Override
    public ResponseDto changeProfile(MultipartFile image) throws IOException {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        fileDeleteComponent.deleteFile(user.getProfilePath());

        String profilePath = imageStorageComponent.saveImage(image, "profiles/");

        user.changeProfilePath(profilePath);

        userRepository.save(user);

        return new ResponseDto(HttpStatus.OK.value(), "프로필 수정이 완료되었습니다.");
    }
}

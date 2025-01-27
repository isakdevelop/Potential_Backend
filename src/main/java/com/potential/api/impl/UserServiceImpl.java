package com.potential.api.impl;

import com.potential.api.common.component.FileDeleteComponent;
import com.potential.api.common.component.ImageStorageComponent;
import com.potential.api.common.component.JwtInformationComponent;
import com.potential.api.common.component.VariableComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.UserEmailRequestDto;
import com.potential.api.dto.request.UserNameRequestDto;
import com.potential.api.dto.request.UserReceiveEmailRequestDto;
import com.potential.api.model.EmailCertification;
import com.potential.api.model.User;
import com.potential.api.repository.EmailCertificationRepository;
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
    private final EmailCertificationRepository emailCertificationRepository;
    private final JwtInformationComponent jwtInformationComponent;
    private final ImageStorageComponent imageStorageComponent;
    private final FileDeleteComponent fileDeleteComponent;
    private final VariableComponent variableComponent;

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
        User user = certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());
        user.changeUserName(userNameRequestDto.getUserName());

        return new ResponseDto(HttpStatus.OK.value(), "닉네임 변경이 완료되었습니다.");
    }

    @Override
    public ResponseDto checkDuplicateEmail(UserEmailRequestDto userEmailRequestDto) {
        if (userRepository.existsByEmail(userEmailRequestDto.getEmail())) {
            throw new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage());
        }

        return new ResponseDto(HttpStatus.OK.value(), "사용 가능한 이메일입니다.");
    }

    @Override
    public ResponseDto validateEmail(UserEmailRequestDto userEmailRequestDto) {
        User user = certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        EmailCertification email = emailCertificationRepository.findById(userEmailRequestDto.getEmail())
                .orElseThrow(() -> new PotentialException(Error.FORBIDDEN.getStatus(), "존재하지 않은 이메일입니다."));

        if (!email.getCertificationNumber().equals(userEmailRequestDto.getPassword())) {
            throw new PotentialException(Error.UNAUTHORIZED.getStatus(), "비밀번호가 일치하지 않습니다.");
        }

        return new ResponseDto(HttpStatus.OK.value(), "메일 인증이 완료되었습니다.");
    }

    @Transactional
    @Override
    public ResponseDto changeUserEmail(UserEmailRequestDto userEmailRequestDto) {
        User user = certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        user.changEmail(userEmailRequestDto.getEmail());

        return new ResponseDto(HttpStatus.OK.value(), "이메일 변경이 완료되었습니다.");
    }

    @Override
    public ResponseDto receiveEmail(UserReceiveEmailRequestDto userReceiveEmailDto) {
        User user = certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        user.changeReceiveEmail(userReceiveEmailDto.getAgree());

        return new ResponseDto(HttpStatus.OK.value(), "이메일 수신 동의 변경이 완료되었습니다.");
    }

    @Override
    public ResponseDto changeProfile(MultipartFile image) throws IOException {
        User user = certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        fileDeleteComponent.deleteFile(user.getProfilePath());

        String profilePath = imageStorageComponent.saveImage(image, "user/");

        user.changeProfilePath(profilePath);

        return new ResponseDto(HttpStatus.OK.value(), "프로필 수정이 완료되었습니다.");
    }


    private User certificationUserJWT(String jwt) {
        return userRepository.findById(jwtInformationComponent.getUserIdFromJWT())
                .orElseThrow(() -> new PotentialException(Error.FORBIDDEN.getStatus(), Error.FORBIDDEN.getMessage()));
    }
}

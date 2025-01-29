package com.potential.api.impl;

import com.potential.api.common.component.ContextBuilderComponent;
import com.potential.api.common.component.JwtInformationComponent;
import com.potential.api.common.component.SendEmailComponent;
import com.potential.api.common.component.VariableComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.EmailRequestDto;
import com.potential.api.dto.request.EmailValidateRequestDto;
import com.potential.api.model.EmailCertification;
import com.potential.api.model.User;
import com.potential.api.repository.EmailCertificationRepository;
import com.potential.api.repository.UserRepository;
import com.potential.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;
    private final EmailCertificationRepository emailCertificationRepository;
    private final JwtInformationComponent jwtInformationComponent;
    private final SendEmailComponent sendEmailComponent;
    private final VariableComponent variableComponent;

    @Override
    public ResponseDto checkDuplicateEmail(EmailRequestDto emailRequestDto) {
        if (userRepository.existsByEmail(emailRequestDto.getEmail())) {
            throw new PotentialException(
                    Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage());
        }

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("사용 가능한 이메일입니다.")
                .build();
    }

    @Override
    public ResponseDto updateEmail(EmailRequestDto emailRequestDto) {
        String password = variableComponent.generateRandomPassword();
        EmailCertification emailCertification = EmailCertification.builder()
                .email(emailRequestDto.getEmail())
                .certificationNumber(password)
                .build();

        emailCertificationRepository.save(emailCertification);

        Context context =  new ContextBuilderComponent()
                .setVariable("title", "이메일 인증 코드")
                .setVariable("subTitle", "이메일 인증 코드 입니다.")
                .setVariable("code", password)
                .build();

        sendEmailComponent.sendEmail(emailRequestDto.getEmail(), "[Potential] 인증 이메일 안내", "emailValidateTemplate", context);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("메일 변경을 위한 메일 발송이 완료되었습니다.")
                .build();
    }

    @Override
    public ResponseDto validateEmail(EmailValidateRequestDto emailValidateRequestDto) {
        if (emailCertificationRepository.findById(emailValidateRequestDto.getEmail())
                .orElseThrow(() -> new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage()))
                .getCertificationNumber().equals(emailValidateRequestDto.getPassword())) {
            return ResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("메일 인증이 완료되었습니다.")
                    .build();
        } else {
            throw new PotentialException(Error.UNAUTHORIZED.getStatus(), "비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    @Override
    public ResponseDto changeUserEmail(EmailRequestDto emailRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        user.changEmail(emailRequestDto.getEmail());

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("이메일 변경이 완료되었습니다.")
                .build();
    }
}

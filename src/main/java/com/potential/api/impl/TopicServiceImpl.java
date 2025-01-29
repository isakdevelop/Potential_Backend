package com.potential.api.impl;

import com.potential.api.common.component.JwtInformationComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.enums.TopicType;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.SwitchSubscriptionRequestDto;
import com.potential.api.dto.request.UpdateSubscriptionRequestDto;
import com.potential.api.model.Subscription;
import com.potential.api.model.Topic;
import com.potential.api.model.User;
import com.potential.api.repository.SubscriptionRepository;
import com.potential.api.repository.TopicRepository;
import com.potential.api.service.TopicService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final JwtInformationComponent jwtInformationComponent;

    @Override
    public ResponseDto switchSubscription(SwitchSubscriptionRequestDto switchSubscriptionRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Topic topics = topicRepository.findTopicsByUserId(user.getId());

        Subscription subscription = subscriptionRepository.findByTopicId(topics.getId());

        subscription.SwitchSubscription(switchSubscriptionRequestDto.getStatus());

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("이메일 수신 동의가 완료되었습니다.")
                .build();
    }

    @Override
    public ResponseDto updateSubscription(UpdateSubscriptionRequestDto updateSubscriptionRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        subscriptionRepository.deleteAllByUser(user);

        List<Subscription> subscriptions = updateSubscriptionRequestDto.getTopics().stream()
                .map(topicName -> {
                    Topic topic = topicRepository.findByTopicType(TopicType.valueOf(topicName))
                            .orElseThrow(() -> new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage()));
                    return Subscription.builder()
                            .user(user)
                            .topic(topic)
                            .topicType(topic.getTopicType())
                            .build();
                })
                .collect(Collectors.toList());

        subscriptionRepository.saveAll(subscriptions);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("구독 목록 수정이 완료되었습니다.")
                .build();
    }
}

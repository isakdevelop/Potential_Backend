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
import com.potential.api.repository.TopicService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements com.potential.api.service.TopicService {
    private final TopicService topicRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final JwtInformationComponent jwtInformationComponent;

    @Transactional
    @Override
    public ResponseDto switchSubscription(SwitchSubscriptionRequestDto switchSubscriptionRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Topic topics = topicRepository.findTopicsByUserId(user.getId());

        Subscription subscription = subscriptionRepository.findByTopicId(topics.getId());

        subscription.SwitchSubscription(switchSubscriptionRequestDto.getStatus());

        subscriptionRepository.save(subscription);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("이메일 수신 동의가 완료되었습니다.")
                .build();
    }

    @Transactional
    @Override
    public ResponseDto updateSubscription(UpdateSubscriptionRequestDto updateSubscriptionRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        List<TopicType> requestedTopics = updateSubscriptionRequestDto.getTopics().stream()
                .map(topicName -> {
                    try {
                        return TopicType.valueOf(topicName);
                    } catch (IllegalArgumentException e) {
                        throw new PotentialException(
                                Error.BAD_REQUEST.getStatus(),
                                Error.BAD_REQUEST.getMessage()
                        );
                    }
                })
                .toList();

        List<Subscription> existingSubscriptions = subscriptionRepository.findAllByUser(user);
        Map<TopicType, Subscription> existingTopicMap = existingSubscriptions.stream()
                .collect(Collectors.toMap(Subscription::getTopicType, subscription -> subscription));

        List<Subscription> subscriptionsToAdd = new ArrayList<>();
        List<Subscription> subscriptionsToRemove = new ArrayList<>(existingSubscriptions);

        for (TopicType requestedTopic : requestedTopics) {
            if (existingTopicMap.containsKey(requestedTopic)) {
                subscriptionsToRemove.remove(existingTopicMap.get(requestedTopic));
            } else {
                Topic topic = topicRepository.findByTopicType(requestedTopic)
                        .orElseThrow(() -> new PotentialException(
                                Error.NOT_FOUND.getStatus(),
                                String.format("Topic not found for type: %s", requestedTopic)
                        ));
                subscriptionsToAdd.add(Subscription.builder()
                        .user(user)
                        .topic(topic)
                        .topicType(topic.getTopicType())
                        .build());
            }
        }

        if (!subscriptionsToRemove.isEmpty()) {
            subscriptionRepository.deleteAll(subscriptionsToRemove); // 삭제
        }
        if (!subscriptionsToAdd.isEmpty()) {
            subscriptionRepository.saveAll(subscriptionsToAdd); // 추가
        }

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("구독 목록이 성공적으로 업데이트되었습니다.")
                .build();
    }
}

package com.potential.api.custom;

import com.potential.api.model.QSubscription;
import com.potential.api.model.QTopic;
import com.potential.api.model.Topic;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicServiceCustomImpl implements TopicServiceCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QTopic topic = QTopic.topic;
    private final QSubscription subscription = QSubscription.subscription;

    @Override
    public Topic findTopicsByUserId(String userId) {
        return jpaQueryFactory
                .selectFrom(topic)
                .join(subscription).on(subscription.topic.eq(topic))
                .where(subscription.user.id.eq(userId))
                .fetchOne();
    }
}

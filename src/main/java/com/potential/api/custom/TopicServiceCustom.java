package com.potential.api.custom;

import com.potential.api.model.Topic;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicServiceCustom {
    Topic findTopicsByUserId(String userId);
}

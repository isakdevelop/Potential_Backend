package com.potential.api.custom;

import com.potential.api.model.Topic;

public interface TopicRepositoryCustom {
    Topic findTopicsByUserId(String userId);
}

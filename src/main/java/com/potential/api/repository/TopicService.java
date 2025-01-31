package com.potential.api.repository;

import com.potential.api.common.enums.TopicType;
import com.potential.api.custom.TopicServiceCustom;
import com.potential.api.model.Topic;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicService extends JpaRepository<Topic, String>, TopicServiceCustom {
    Optional<Topic> findByTopicType(TopicType topicType);
}

package com.potential.api.repository;

import com.potential.api.model.Subscription;
import com.potential.api.model.Topic;
import com.potential.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, String> {
    Subscription findByTopicId(String topic_id);

    void deleteAllByUser(User user);
}

package com.potential.api.repository;

import com.potential.api.model.Post;
import com.potential.api.model.PostHeart;
import com.potential.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHeartRepository extends JpaRepository<PostHeart, String> {
    boolean existsByPostAndUser(Post post, User user);

    void deleteByPostAndUser(Post post, User user);
}

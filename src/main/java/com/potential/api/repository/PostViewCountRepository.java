package com.potential.api.repository;

import com.potential.api.model.PostViewCount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostViewCountRepository extends CrudRepository<PostViewCount, String> {
    boolean existsByClientIpAndPostId(String clientIp, String postId);
}

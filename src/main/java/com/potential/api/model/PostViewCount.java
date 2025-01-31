package com.potential.api.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "viewCount", timeToLive = 86400)
public class PostViewCount {
    @Id
    private String clientIp;
    private String postId;
}

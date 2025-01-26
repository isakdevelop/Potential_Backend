package com.potential.api.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@RedisHash(value = "email", timeToLive = 300)
public class EmailCertification {
    @Id
    private String email;
    private String certificationNumber;
}

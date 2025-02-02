package com.potential.api.dto.request;

import lombok.Getter;

@Getter
public class PostToggleStatusRequestDto {
    private String postId;
    private String afterStatus;
}

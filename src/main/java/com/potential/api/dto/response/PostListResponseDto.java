package com.potential.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostListResponseDto {
    private Long id;
    private String title;
    private String userName;
    private String postTime;
    private String viewCount;
    private String commentCount;
    private String likeCount;
}

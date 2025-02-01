package com.potential.api.dto.request;

import lombok.Getter;

@Getter
public class WriteCommentRequestDto {
    private String postId;
    private String userName;
    private String content;
}

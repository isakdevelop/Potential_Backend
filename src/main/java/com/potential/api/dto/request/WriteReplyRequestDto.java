package com.potential.api.dto.request;


import lombok.Getter;

@Getter
public class WriteReplyRequestDto {
    private String postId;
    private String commentId;
    private String reply;
}

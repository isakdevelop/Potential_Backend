package com.potential.api.service;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.WriteCommentRequestDto;
import com.potential.api.dto.request.WriteReplyRequestDto;

public interface CommentService {
    ResponseDto writeComment(WriteCommentRequestDto writeCommentRequestDto);

    ResponseDto writeReply(WriteReplyRequestDto writeReplyRequestDto);
}

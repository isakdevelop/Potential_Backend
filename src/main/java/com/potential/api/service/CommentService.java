package com.potential.api.service;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.WriteCommentRequestDto;

public interface CommentService {
    ResponseDto writeComment(WriteCommentRequestDto writeCommentRequestDto);
}

package com.potential.api.controller;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.WriteCommentRequestDto;
import com.potential.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/comment")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/postComment")
    public ResponseEntity<ResponseDto> writeComment(@RequestBody WriteCommentRequestDto writeCommentRequestDto) {
        return ResponseEntity.ok(commentService.writeComment(writeCommentRequestDto));
    }
}

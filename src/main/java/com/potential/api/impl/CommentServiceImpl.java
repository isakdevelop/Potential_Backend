package com.potential.api.impl;

import com.potential.api.common.component.JwtInformationComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.WriteCommentRequestDto;
import com.potential.api.dto.request.WriteReplyRequestDto;
import com.potential.api.model.Comment;
import com.potential.api.model.Post;
import com.potential.api.model.User;
import com.potential.api.repository.CommentRepository;
import com.potential.api.repository.PostRepository;
import com.potential.api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final JwtInformationComponent jwtInformationComponent;
    private final PostRepository postRepository;

    @Transactional
    @Override
    public ResponseDto writeComment(WriteCommentRequestDto writeCommentRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Post post = postRepository.findById(writeCommentRequestDto.getPostId())
                .orElseThrow(() -> new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage()));

        Comment comment = Comment.builder()
                .content(writeCommentRequestDto.getContent())
                .post(post)
                .user(user)
                .build();

        commentRepository.save(comment);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("댓글 작성이 완료되었습니다.")
                .build();
    }

    @Override
    public ResponseDto writeReply(WriteReplyRequestDto writeReplyRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Post post = postRepository.findById(writeReplyRequestDto.getPostId())
                .orElseThrow(() -> new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage()));

        Comment comment = Comment.builder()
                .content(writeReplyRequestDto.getCommentId())
                .post(post)
                .user(user)
                .parent(commentRepository.findById(writeReplyRequestDto.getCommentId()).orElseThrow())
                .build();

        commentRepository.save(comment);


        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("대댓글 작성이 완료되었습니다.")
                .build();
    }
}

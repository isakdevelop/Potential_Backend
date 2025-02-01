package com.potential.api.controller;

import com.potential.api.custom.PostServiceCustom;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.PostDetailsRequestDto;
import com.potential.api.dto.request.PostToggleHeartRequestDto;
import com.potential.api.dto.request.WritePostRequestDto;
import com.potential.api.dto.response.PostListResponseDto;
import com.potential.api.dto.response.PostResponseDto;
import com.potential.api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostServiceCustom postServiceCustom;

    @PostMapping("/writePost")
    public ResponseEntity<ResponseDto> writePost(@RequestBody WritePostRequestDto writePostRequestDto) {
        return ResponseEntity.ok(postService.writePost(writePostRequestDto));
    }

    @GetMapping("/postList")
    public ResponseEntity<Page<PostListResponseDto>> postList(Pageable pageable) {
        return ResponseEntity.ok(postServiceCustom.list(pageable));
    }

    @GetMapping("/postDetails")
    public ResponseEntity<PostResponseDto> postDetails(@RequestBody PostDetailsRequestDto postDetailsRequestDto) {
        return ResponseEntity.ok(postService.postDetails(postDetailsRequestDto));
    }

    @PostMapping("/toggleHeart")
    public ResponseEntity<ResponseDto> toggleHeart(@RequestBody PostToggleHeartRequestDto postToggleHeartRequestDto) {
        return ResponseEntity.ok(postService.toggleHeart(postToggleHeartRequestDto));
    }

}

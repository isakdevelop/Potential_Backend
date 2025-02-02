package com.potential.api.service;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.PostDetailsRequestDto;
import com.potential.api.dto.request.PostToggleHeartRequestDto;
import com.potential.api.dto.request.PostToggleStatusRequestDto;
import com.potential.api.dto.request.WritePostRequestDto;
import com.potential.api.dto.response.PostResponseDto;

public interface PostService {
    ResponseDto writePost(WritePostRequestDto writePostRequestDto);

    PostResponseDto postDetails(PostDetailsRequestDto postDetailsRequestDto);

    ResponseDto toggleHeart(PostToggleHeartRequestDto postToggleHeartRequestDto);

    ResponseDto toggleStatus(PostToggleStatusRequestDto postToggleStatusRequestDto);
}

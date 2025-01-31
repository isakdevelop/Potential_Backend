package com.potential.api.custom;

import com.potential.api.dto.response.PostListResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostServiceCustom {
    Page<PostListResponseDto> list(Pageable pageable);
}

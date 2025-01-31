package com.potential.api.impl;

import com.potential.api.common.component.ClientIpComponent;
import com.potential.api.common.component.JwtInformationComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.PostDetailsRequestDto;
import com.potential.api.dto.request.WritePostRequestDto;
import com.potential.api.dto.response.PostResponseDto;
import com.potential.api.model.Post;
import com.potential.api.model.PostViewCount;
import com.potential.api.model.User;
import com.potential.api.repository.PostRepository;
import com.potential.api.repository.PostViewCountRepository;
import com.potential.api.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostViewCountRepository postViewCountRepository;
    private final JwtInformationComponent jwtInformationComponent;
    private final ClientIpComponent clientIpComponent;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    @Override
    public ResponseDto writePost(WritePostRequestDto writePostRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Post post = Post.builder()
                .title(writePostRequestDto.getTitle())
                .content(writePostRequestDto.getContext())
                .user(user)
                .viewCount(0)
                .build();

        postRepository.save(post);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("게시글 작성이 완료돠었습니다.")
                .build();
    }

    @Transactional
    @Override
    public PostResponseDto postDetails(PostDetailsRequestDto postDetailsRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Post post = postRepository.findById(postDetailsRequestDto.getPostId())
                .orElseThrow(() -> new PotentialException(Error.NOT_FOUND.getStatus(), Error.NOT_FOUND.getMessage()));

        boolean isViewAlreadyExists = postViewCountRepository.existsByClientIpAndPostId(clientIpComponent.getClientIp(), post.getId());

        if (!isViewAlreadyExists) {
            PostViewCount postViewCount = PostViewCount.builder()
                    .clientIp(clientIpComponent.getClientIp())
                    .postId(post.getId())
                    .build();

            postViewCountRepository.save(postViewCount);

            post.incrementViewCount();

        }
        return PostResponseDto.builder()
                .postId(post.getId())
                .userName(post.getUser().getUserName())
                .title(post.getTitle())
                .content(post.getContent())
                .createAt(post.getCreateAt())
                .updateAt(post.getUpdateAt())
                .viewCount(post.getViewCount())
                .build();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearRedisPostCount() {
        redisTemplate.delete("clientIp");
    }
}

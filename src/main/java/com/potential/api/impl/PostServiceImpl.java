package com.potential.api.impl;

import com.potential.api.common.component.ClientIpComponent;
import com.potential.api.common.component.JwtInformationComponent;
import com.potential.api.common.enums.Error;
import com.potential.api.common.enums.PostStatus;
import com.potential.api.common.exception.PotentialException;
import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.PostDeleteRequestDto;
import com.potential.api.dto.request.PostDetailsRequestDto;
import com.potential.api.dto.request.PostToggleHeartRequestDto;
import com.potential.api.dto.request.PostToggleStatusRequestDto;
import com.potential.api.dto.request.WritePostRequestDto;
import com.potential.api.dto.response.PostResponseDto;
import com.potential.api.model.Post;
import com.potential.api.model.PostHeart;
import com.potential.api.model.PostViewCount;
import com.potential.api.model.User;
import com.potential.api.repository.PostHeartRepository;
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
    private final PostHeartRepository postHeartRepository;
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
                .heart_count(0)
                .postStatus(PostStatus.RECRUITMENT_IN_PROGRESS)
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

    @Transactional
    @Override
    public ResponseDto toggleHeart(PostToggleHeartRequestDto postToggleHeartRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Post post = postRepository.findById(postToggleHeartRequestDto.getPostId())
                .orElseThrow(() -> new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage()));

        boolean isHearted = postHeartRepository.existsByPostAndUser(post, user);

        if(isHearted) {
            postHeartRepository.deleteByPostAndUser(post, user);
            post.decrementHeartCount();
        } else {
            PostHeart postHeart = PostHeart.builder()
                    .post(post)
                    .user(user)
                    .build();

            postHeartRepository.save(postHeart);
            post.incrementHeartCount();
        }

        postRepository.save(post);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(isHearted ? "하트를 취소했습니다." : "하트를 눌렀습니다.")
                .build();
    }

    @Transactional
    @Override
    public ResponseDto toggleStatus(PostToggleStatusRequestDto postToggleStatusRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());
        Post post = postRepository.findById(postToggleStatusRequestDto.getPostId())
                .orElseThrow(() -> new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage()));

        post.ToggleStatus(PostStatus.valueOf(postToggleStatusRequestDto.getAfterStatus()));

        postRepository.save(post);

        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message("게시글 상태 변경이 완료되었습니다.")
                .build();
    }

    @Transactional
    @Override
    public ResponseDto deletePost(PostDeleteRequestDto postDeleteRequestDto) {
        User user = jwtInformationComponent.certificationUserJWT(jwtInformationComponent.getUserIdFromJWT());

        Post post = postRepository.findById(postDeleteRequestDto.getPostId())
                .orElseThrow(() -> new PotentialException(Error.CONFLICT.getStatus(), Error.CONFLICT.getMessage()));

        if (post.getUser().getId().equals(user.getId())) {
            postRepository.delete(post);

            return ResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .message("게시글 삭제가 완료되었습니다.")
                    .build();
        } else {
            return ResponseDto.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .message("게시글 삭제에 대한 권한이 없습니다.")
                    .build();
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearRedisPostCount() {
        redisTemplate.delete("clientIp");
    }
}

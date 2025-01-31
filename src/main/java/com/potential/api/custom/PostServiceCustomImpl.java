package com.potential.api.custom;

import com.potential.api.dto.response.PostListResponseDto;
import com.potential.api.model.QPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceCustomImpl implements PostServiceCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QPost post = QPost.post;

    @Override
    public Page<PostListResponseDto> list(Pageable pageable) {
        Long total = jpaQueryFactory
                .select(post.count())
                .from(post)
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);

        List<PostListResponseDto> list = jpaQueryFactory
                .select(Projections.constructor(
                                PostListResponseDto.class,
                                post.id,
                                post.title,
                                post.user.userName
                        )
                )
                .from(post)
                .orderBy(post.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(list, pageable, totalCount);
    }
}

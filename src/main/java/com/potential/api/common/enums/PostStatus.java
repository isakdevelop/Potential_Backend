package com.potential.api.common.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostStatus {
    RECRUITMENT_IN_PROGRESS("모집중"),
    RECRUITMENT_COMPLETED("모집완료");

    private final String postStatus;
}

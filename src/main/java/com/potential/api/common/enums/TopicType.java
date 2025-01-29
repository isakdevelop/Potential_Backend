package com.potential.api.common.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TopicType {
    BACKEND("백엔드"),
    FRONTEND("프론트엔드"),
    ANDROID("안드로이드"),
    IOS("IOS"),
    DEVOPS("데브옵스"),
    PM("프로젝트매니저"),
    DESIGNER("디자이너");

    private final String topic;
}

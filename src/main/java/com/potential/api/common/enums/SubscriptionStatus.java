package com.potential.api.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SubscriptionStatus {
    Agree("동의"),
    Disagree("비동의")
    ;

    private final String subscriptionStatus;
}

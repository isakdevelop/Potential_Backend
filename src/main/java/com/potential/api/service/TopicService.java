package com.potential.api.service;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.SwitchSubscriptionRequestDto;
import com.potential.api.dto.request.UpdateSubscriptionRequestDto;

public interface TopicService {
    ResponseDto switchSubscription(SwitchSubscriptionRequestDto switchSubscriptionRequestDto);

    ResponseDto updateSubscription(UpdateSubscriptionRequestDto updateSubscriptionRequestDto);
}

package com.potential.api.controller;

import com.potential.api.dto.ResponseDto;
import com.potential.api.dto.request.SwitchSubscriptionRequestDto;
import com.potential.api.dto.request.UpdateSubscriptionRequestDto;
import com.potential.api.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/topic")
public class TopicController {
    private final TopicService topicService;

    @PatchMapping("/switchSubscription")
    ResponseEntity<ResponseDto> switchSubscription(@RequestBody SwitchSubscriptionRequestDto switchSubscriptionRequestDto) {
        return ResponseEntity.ok(topicService.switchSubscription(switchSubscriptionRequestDto));
    }

    @PatchMapping("/updateSubscription")
    ResponseEntity<ResponseDto> updateSubscription(@RequestBody UpdateSubscriptionRequestDto updateSubscriptionRequestDto) {
        return ResponseEntity.ok(topicService.updateSubscription(updateSubscriptionRequestDto));
    }
}

package com.potential.api.dto.request;

import java.util.List;
import lombok.Getter;

@Getter
public class UpdateSubscriptionRequestDto {
    private List<String> topics;
}

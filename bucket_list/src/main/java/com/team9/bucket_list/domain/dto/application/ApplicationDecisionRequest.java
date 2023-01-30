package com.team9.bucket_list.domain.dto.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationDecisionRequest {
    private Long id;
    private Long memberId;
    private byte status;
}

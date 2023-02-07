package com.team9.bucket_list.domain.dto.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationDecisionRequest {
    private Long applicationId; //신청서 아이디
    private byte status; //업데이트할 상태
}

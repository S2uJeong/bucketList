package com.team9.bucket_list.domain.dto.application;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationAcceptCountResponse {
    private Long postId;
    private int count;

    public static ApplicationAcceptCountResponse response(Long postId, int count) {
        return ApplicationAcceptCountResponse.builder()
                .postId(postId)
                .count(count)
                .build();
    }
}

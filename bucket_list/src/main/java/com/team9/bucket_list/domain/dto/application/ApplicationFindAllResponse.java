package com.team9.bucket_list.domain.dto.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFindAllResponse {
    private List<ApplicationListResponse> list;
    private int count;
}

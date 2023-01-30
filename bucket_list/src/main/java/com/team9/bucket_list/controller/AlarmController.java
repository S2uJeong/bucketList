package com.team9.bucket_list.controller;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.AlarmResponse;
import com.team9.bucket_list.domain.entity.Alarm;
import com.team9.bucket_list.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class AlarmController {

    private AlarmService alarmService;

    @GetMapping("/{memberId}/alarms")
    public Response<Page<AlarmResponse>> alarmList(@PathVariable Long memberId, @PageableDefault(sort = "id", size = 5, direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        String userName = authentication.getName();
        Page<AlarmResponse> result = alarmService.list(memberId, userName, pageable);
        return Response.success(result);
    }
}

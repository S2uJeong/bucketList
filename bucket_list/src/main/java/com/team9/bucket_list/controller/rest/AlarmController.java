package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("alarm")
@Slf4j
public class AlarmController {

    private final AlarmService alarmService;

    //알람 리스트 받아오기
    @GetMapping
    public Response alarmList(@PageableDefault(size = 10) /* @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)*/ Pageable pageable) {
        //로그인된 아이디의 알람 리스트
        Long memberId = 1L;

        return Response.success(alarmService.alarmList(pageable, memberId));
    }

    //알람 카운트 받아오기
    @GetMapping
    public Response alarmCount() {
        //로그인된 아이디의 알람 리스트
        Long memberId = 1L;

        return Response.success(alarmService.alarmCount(memberId));
    }

}

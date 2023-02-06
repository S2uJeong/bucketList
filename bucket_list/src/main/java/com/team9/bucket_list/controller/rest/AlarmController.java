package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("alarm")
@Slf4j
@Tag(name = "알람", description = "게시글에 신청서가 오거나 좋아요가 눌리면 member에게 알람이 갑니다.")
public class AlarmController {

    private final AlarmService alarmService;

    //알람 리스트 받아오기
    @GetMapping
    @Operation(summary = "알람 리스트 조회", description = "로그인 되어있는 멤버의 id를 이용하여 조회합니다. 최신 알람일수록 위로 정렬됩니다.")
    public Response alarmList(@Parameter(hidden = true) @PageableDefault(size = 10) /* @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)*/ Pageable pageable) {
        //로그인된 아이디의 알람 리스트
        Long memberId = 1L;

        return Response.success(alarmService.alarmList(pageable, memberId));
    }

    //알람 카운트 받아오기
    @GetMapping("count")
    @Operation(summary = "알람 카운트 조회", description = "로그인 되어있는 멤버의 id를 이용하여 조회합니다.")
    public Response alarmCount() {
        //로그인된 아이디의 알람 리스트
        Long memberId = 1L;

        return Response.success(alarmService.alarmCount(memberId));
    }

    //알람을 읽었을대
    @GetMapping("/{alarmId}")
    @Operation(summary = "특정 알람 조회", description = "알람 id를 이용하여 조회합니다.")
    public Response alarmRead(@Parameter(name = "alarmId", description = "알람 id", in = ParameterIn.PATH) @PathVariable Long alarmId) {
        //로그인된 아이디의 알람 리스트
        Long memberId = 1L;

        return Response.success(alarmService.alarmRead(memberId, alarmId));
    }
}

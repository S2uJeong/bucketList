package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("alarms")
@Slf4j
@Tag(name = "알람", description = "게시글에 신청서가 오거나 좋아요가 눌리면 member에게 알람이 갑니다.")
public class AlarmRestController {

    private final AlarmService alarmService;

    //알람 리스트 받아오기
    @GetMapping
    @Operation(summary = "알람 리스트 조회", description = "로그인 되어있는 멤버의 id를 이용하여 조회합니다. 최신 알람일수록 위로 정렬됩니다.")
    public Response alarmList(@Parameter(hidden = true) @PageableDefault(size = 10) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        log.info("알람 리스트 컨트롤러");
        return Response.success(alarmService.alarmList(pageable, Long.valueOf(authentication.getName())));
    }

    //알람 카운트 받아오기
    @GetMapping("count")
    @Operation(summary = "알람 카운트 조회", description = "로그인 되어있는 멤버의 id를 이용하여 조회합니다.")
    public Response alarmCount(Authentication authentication) {
        return Response.success(alarmService.alarmCount(Long.valueOf(authentication.getName())));
    }

    //알람을 읽었을때
    @PostMapping
    @Operation(summary = "알람 읽음 처리", description = "알람 id를 이용하여 조회합니다.")
    public Response alarmRead(@RequestParam Long alarmId, Authentication authentication) {
        return Response.success(alarmService.alarmRead(Long.valueOf(authentication.getName()), alarmId));
    }

    //새로운 알림이 등록되었을때
    @GetMapping("/new")
    @Operation(summary = "새로운 알림 불러오기", description = "알람 id와 memberId를 이용하여 조회합니다.")
    public Response newAlarmList(@Parameter(hidden = true) @PageableDefault(size = 10) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication, @RequestParam Long id) {
        return Response.success(alarmService.newAlarmList(pageable, Long.valueOf(authentication.getName()),id));
    }

    //스크롤로 새로운 알람을 받아올때
    @GetMapping("/new/scroll")
    @Operation(summary = "새로운 알림 불러오기", description = "알람 id와 memberId를 이용하여 조회합니다.")
    public Response newAlarmListScroll(@Parameter(hidden = true) @PageableDefault(size = 10) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication, @RequestParam Long id) {
        return Response.success(alarmService.newAlarmListScroll(pageable, Long.valueOf(authentication.getName()),id));
    }

    //알람 전부 읽기
    @PostMapping("/all")
    @Operation(summary = "알람 모두 읽기", description = "memberId를 이용하여 수정합니다.")
    public Response realAllAlarm(Authentication authentication) {
        return Response.success(alarmService.realAllAlarm(Long.valueOf(authentication.getName())));
    }
}

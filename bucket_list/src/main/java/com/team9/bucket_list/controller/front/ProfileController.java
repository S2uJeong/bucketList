package com.team9.bucket_list.controller.front;

import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.repository.MemberReviewRepository;
import com.team9.bucket_list.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;


    //=== 프로필 조회 ===//
    @GetMapping("/{memberId}")
    public String readDetail(@PathVariable("memberId") Long memberId){
        return "Profile/profile";
    }

    //=== 프로필 수정 ===//
        // edit 할 수 있는지 권한 확인 및 파일 올리는 화면으로 연결
//    @GetMapping("/{memberId}/edit")
//    public String updateProfile(@PathVariable("memberId") Long memberId,
//                                Authentication authentication) {
//        // 유효성 검사 : 로그인한 멤버와 수정 대상인 프로필의 memberId가 일치 한지 확인한다.
//        Long loginedMemberId =  Long.valueOf(authentication.getName());
//        profileService.checkAuthority(memberId, loginedMemberId);
//        return "Profile/profileUpdate"; // 파일 수정 뒤에 다시 profile 보게 가려고 하면 어떻게 하지?
//    }
        // 수정내용 불러 올 부분
/*    @GetMapping(value = "/{memberId}/json", produces = "application/json")
    @ResponseBody
    // 사진 값만 들어있는 알맏은 Dto로 변경
    public Response<ProfileReadResponse> jsonRead(@PathVariable(value = "memberId") Long memberId){
        ProfileReadResponse response = profileService.detail(memberId); // Dto 변경해서 받으면 됨.

        return Response.success(response);

    }*/

}

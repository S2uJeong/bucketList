package com.team9.bucket_list.controller.front;

import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.MemberReview;
import com.team9.bucket_list.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private ProfileService profileService;

    // 프로필 조회
    @GetMapping("/{memberId}")
    public String profile(@PathVariable Long memberId, Model model) {

//        db에 user가 없어서 임의로 member를 작성했습니다
//        Member member = profileService.checkMemberId(memberId);
        Member member = Member.builder()
                .userName("jihwan byeon")
                .email("jihwan@naver.com")
                .build();

        List<MemberReview> memberReviewList = (List<MemberReview>) MemberReview.builder()
                .member(member)
                .rate(4.2)
                .build();

        model.addAttribute("member", member);
        model.addAttribute("rev iew", memberReviewList);
        return "profile";
    }


//    // 타인의 프로필 조회
//    @GetMapping("/{memberId}")
//    public String profile(@PathVariable Long memberId, Authentication authentication, Model model) {
//        model.addAttribute("userName", authentication.getName());
//        model.addAttribute("memberId", memberId);
//        return "profile";
//    }

//    // 본인 프로필 조회 -> 수정
//    @GetMapping("/{memberId}/edit")
//    public String editMyProfile(@PathVariable Long memberId, Model model, Authentication authentication) {
//
//
//        String userName = authentication.getName();
//        Member member = profileService.checkMemberId(memberId);
//
//        profileService.update(memberId, userName, )
//
//        model.addAttribute("member", member);
//        model.addAttribute("mypageEditRequest", new MyPageEditRequest());
//
//        return "/Profile/ProfileUpdate";
//    }

    // 본인 프로필 수정 -> 수정된 페이지 확인
    @PostMapping("/{memberId}/edit")
    public String wrtieProfile(@PathVariable Long memberId, Authentication authentication, MultipartFile multipartFile) {
        String userName = authentication.getName();
        profileService.update(memberId, userName, multipartFile);
        return "redirect:/profile/detail/{memberId}"; // memberId 이렇게 적는 게 맞나?
    }
}

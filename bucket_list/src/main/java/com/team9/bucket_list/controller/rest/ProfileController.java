package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.profile.ProfileResponse;
import com.team9.bucket_list.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{memberId}")
    public Response<List<ProfileResponse>> profile(@PathVariable Long memberId) {
        List<ProfileResponse> result = profileService.detail(memberId);
        return Response.success(result);
    }

    @PutMapping("/{memberId}")
    public Response<String> editProfileImage(@PathVariable Long memberId, Authentication authentication, MultipartFile multipartFile) {
        String userName = authentication.getName();
        String result = profileService.update(memberId, userName, multipartFile);
        return Response.success(result);
    }
}

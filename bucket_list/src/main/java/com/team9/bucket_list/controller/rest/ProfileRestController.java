package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.profile.ProfileResponse;
import com.team9.bucket_list.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/member/profile")
@RequiredArgsConstructor
@Tag(name = "멤버 프로필", description = "타인이 볼 수 있는 멤버의 프로필입니다. 멤버 평가가 표시됩니다.")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{memberId}")
    @Operation(summary = "프로필 조회", description = "해당 멤버의 프로필을 출력합니다.")
    public Response<List<ProfileResponse>> profile(@Parameter(name = "memberId", description = "멤버 id") @PathVariable Long memberId) {
        List<ProfileResponse> result = profileService.detail(memberId);
        return Response.success(result);
    }

    @PutMapping(value = "/{memberId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 수정", description = "로그인된 멤버 본인의 프로필을 수정합니다.")
    public Response<ProfileResponse> updateProfileImage(@Parameter(name = "memberId", description = "로그인 한 멤버 id") @PathVariable Long memberId,
                                             Authentication authentication,
                                             @RequestPart(value="file",required = false) MultipartFile multipartFile) {
        String userName = authentication.getName();
        ProfileResponse profileResponse = profileService.update(memberId, userName, multipartFile);
        return Response.success(profileResponse);
    }
}

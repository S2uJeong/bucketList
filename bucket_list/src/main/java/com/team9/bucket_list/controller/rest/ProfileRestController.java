package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.profile.ProfileEditResponse;
import com.team9.bucket_list.domain.dto.profile.ProfileReadResponse;
import com.team9.bucket_list.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/profile")
@RequiredArgsConstructor
@Tag(name = "멤버 프로필", description = "타인이 볼 수 있는 멤버의 프로필입니다. 멤버 평가가 표시됩니다.")
public class ProfileRestController {

    private final ProfileService profileService;

    @GetMapping("/{memberId}/json")
    @Operation(summary = "프로필 조회", description = "해당 멤버의 프로필을 출력합니다.")
    public Response<ProfileReadResponse> read(@Parameter(name = "memberId", description = "멤버 id") @PathVariable Long memberId) {
        ProfileReadResponse response = profileService.read(memberId);
        return Response.success(response);
    }

    @PostMapping(value = "/{memberId}/edit}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "프로필 수정", description = "로그인된 멤버 본인의 프로필을 수정합니다.")
    public Response<ProfileEditResponse> update(@Parameter(name = "memberId", description = "멤버 id") @PathVariable Long memberId,
                                                            @RequestPart(value="file",required = false) @Valid @RequestParam("file") MultipartFile file) {
        // Long loginedMemberId =  Long.valueOf(authentication.getName());
        log.info(file.getName());
        ProfileEditResponse profileEditResponse = profileService.update(memberId,file);
        return Response.success(profileEditResponse);
    }
}

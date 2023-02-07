package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.profile.ProfileResponse;
import com.team9.bucket_list.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileRestController {

    private final ProfileService profileService;

    @GetMapping("/{memberId}")
    public Response<List<ProfileResponse>> profile(@PathVariable Long memberId) {
        List<ProfileResponse> result = profileService.detail(memberId);
        return Response.success(result);
    }

    @PutMapping(value = "/{memberId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<ProfileResponse> updateProfileImage(@PathVariable Long memberId, Authentication authentication, @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        String userName = authentication.getName();
        ProfileResponse profileResponse = profileService.update(memberId, userName, multipartFile);
        return Response.success(profileResponse);
    }
}

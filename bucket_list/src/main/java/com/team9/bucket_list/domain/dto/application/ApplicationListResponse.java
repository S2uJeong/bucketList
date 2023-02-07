package com.team9.bucket_list.domain.dto.application;

import com.team9.bucket_list.domain.entity.Application;
import com.team9.bucket_list.domain.enumerate.Gender;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ApplicationListResponse {
    private Long id;
    private Long memberId;
    private String userName;
    private int age;
    private Gender gender;
    private String content;

    public static Page<ApplicationListResponse> pageList(Page<Application> applicationPage) {
        return applicationPage.map( application -> ApplicationListResponse.builder()
                .id(application.getId())
                .memberId(application.getMember().getId())
                .userName(application.getMember().getUserName())
                .age(application.getMember().getAge())
                .gender(application.getMember().getGender())
                .content(application.getContent())
                .build());
    }

    public static List<ApplicationListResponse> applicationListResponseList(List<Application> applicationList) {
        List<ApplicationListResponse> applicationListResponseList = new ArrayList<>();

        for (Application application : applicationList) {
            applicationListResponseList.add(ApplicationListResponse.builder()
                    .id(application.getId())
                    .memberId(application.getMember().getId())
                    .userName(application.getMember().getUserName())
                    .age(application.getMember().getAge())
                    .gender(application.getMember().getGender())
                    .content(application.getContent())
                    .build());
        }

        return applicationListResponseList;
    }
}

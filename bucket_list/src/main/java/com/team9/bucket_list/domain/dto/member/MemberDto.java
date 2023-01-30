package com.team9.bucket_list.domain.dto.member;

import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.domain.enumerate.Gender;
import com.team9.bucket_list.domain.enumerate.MemberRole;
import com.team9.bucket_list.domain.enumerate.Membership;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String password;
    private String userName;
    private int age;
    private int postRemain;

    private Gender gender;
    private MemberRole memberRole;
    private Membership membership;

    private List<Post> postList;
//    private List<Application> applicationList;
//    private List<Likes> likesList;
//    private List<MemberReview> memberReviewList;
//    private List<Alarm> alarmList;
//    private List<MemberBucketlist> memberBucketlistList;
}

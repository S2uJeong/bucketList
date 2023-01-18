package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto.Member.MemberDto;
import com.team9.bucket_list.domain.enumerate.Gender;
import com.team9.bucket_list.domain.enumerate.Membership;
import com.team9.bucket_list.domain.enumerate.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String username;
    private int age;
    private int postRemain;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private UserRole userRole;

    @Enumerated(value = EnumType.STRING)
    private Membership membership;

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberReview> memberReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Alarm> alarmList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberBucketlist> memberBucketlistList = new ArrayList<>();

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(id)
                .email(email)
                .password(password)
                .username(username)
                .age(age)
                .postRemain(postRemain)
                .gender(gender)
                .userRole(userRole)
                .membership(membership)
                .postList(postList)
                .applicationList(applicationList)
                .likesList(likesList)
                .memberReviewList(memberReviewList)
                .alarmList(alarmList)
                .memberBucketlistList(memberBucketlistList)
                .build();
    }
}

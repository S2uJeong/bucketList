package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto. member.MemberDto;
import com.team9.bucket_list.domain.enumerate.Gender;
import com.team9.bucket_list.domain.enumerate.MemberRole;
import com.team9.bucket_list.domain.enumerate.Membership;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String userName;
    private int age;
    private int postRemain;

    // profile에 이미지 넣는 거 구현중
    private String image;

    /**OAuth2 적용**/
    private String oauthId;

    public Member update(String email){
        this.userName = email.split("@")[0];
        this.email = email;
        return this;
    }

    public void updateProfileImage(String image) {
        this.image = image;
    }

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private MemberRole memberRole;

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

    @OneToMany(mappedBy = "member")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    public MemberDto toDto() {
        return MemberDto.builder()
                .id(id)
                .email(email)
                .password(password)
                .userName(userName)
                .age(age)
                .postRemain(postRemain)
                .gender(gender)
                .memberRole(memberRole)
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

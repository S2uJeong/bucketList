package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto. member.MemberDto;
import com.team9.bucket_list.domain.dto.member.MemberProfile;
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
    private double rate;

    public void rateUpdate(double avg) {
        rate = avg;
    }


    // 프로필 사진
    // 사진을 서버의 특정 폴더에 저장
    @Column(name = "post_image_url")
    private String postImageUrl;

    public void updateProfileImage(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }


    /**
     * OAuth2 적용
     **/
    private String oauthId;

    public Member updateGoogle(String email){
        this.userName = email.split("@")[0];
        this.email = email;
        return this;
    }

    public Member updateNaver(String email, String gender, String birthYear){
        this.userName = email.split("@")[0];
        this.email = email;
        this.gender = MemberProfile.getGender(gender);
        this.age = MemberProfile.getAge(birthYear);
        return this;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberReview> memberReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Alarm> alarmList = new ArrayList<>();

//    @OneToMany(mappedBy = "member")
//    private List<MemberBucketlist> memberBucketlistList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ChatParticipant> chatParticipants = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Profile profile;

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
                .build();
    }

}

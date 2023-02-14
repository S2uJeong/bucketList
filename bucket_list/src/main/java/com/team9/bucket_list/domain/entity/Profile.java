package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.repository.MemberRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    private String uploadFileName; // 사용자가 설정한 파일 이름
    private String awsS3FileName; // DB에 저장된 파일의 URL

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private String email;
    private double avgRate;

    public static Profile save(String updloadFileName, String awsS3FileName, double avgRate, Member member) {
        return Profile.builder()
                .uploadFileName(updloadFileName)
                .awsS3FileName(awsS3FileName)
                .member(member)
                .email(member.getEmail())
                .avgRate(avgRate)
                .build();
    }

    public static Profile updateImage(String updloadFileName, String awsS3FileName, Profile profile) {
        return  Profile.builder()
                .id(profile.getId())
                .uploadFileName(updloadFileName)
                .awsS3FileName(awsS3FileName)
                .member(profile.getMember())
                .email(profile.getEmail())
                .avgRate(profile.getAvgRate())
                .build();
    }

    public static Profile updateRate(double avgRate, Profile profile) {
        return  Profile.builder()
                .id(profile.getId())
                .uploadFileName(profile.getUploadFileName())
                .awsS3FileName(profile.getAwsS3FileName())
                .member(profile.getMember())
                .email(profile.getEmail())
                .avgRate(avgRate)
                .build();
    }

}

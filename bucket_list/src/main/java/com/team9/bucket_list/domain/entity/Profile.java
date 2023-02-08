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

    @OneToOne
    private Member member;

    private double rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private MemberReview memberReview;

    public static Profile save(String updloadFileName, String awsS3FileName, Member member) {
        return Profile.builder()
                .uploadFileName(updloadFileName)
                .awsS3FileName(awsS3FileName)
                .member(member)
                .build();

    }

}

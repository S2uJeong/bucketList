package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.repository.MemberRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_image_url")
    private Member postImageUrl;

    @OneToOne
    @JoinColumn(name = "email")
    private Member member;

    private double rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title")
    private MemberReview memberReview;

}

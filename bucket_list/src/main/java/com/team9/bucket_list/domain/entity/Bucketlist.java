package com.team9.bucket_list.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Bucketlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucketlist_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;


    @OneToMany(mappedBy = "bucketlist")
    private List<MemberBucketlist> memberBucketlistList = new ArrayList<>();

    @OneToMany(mappedBy = "bucketlist")
    private List<BucketlistReview> bucketlistReviewList = new ArrayList<>();

    public void update(String content) {
        post.modifiedContent(content);
    }
}

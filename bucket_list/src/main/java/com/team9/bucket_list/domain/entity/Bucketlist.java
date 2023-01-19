package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Bucketlist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucketlist_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(mappedBy = "bucketlist")
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "bucketlist")
    private List<MemberBucketlist> memberBucketlistList = new ArrayList<>();

    @OneToMany(mappedBy = "bucketlist")
    private List<BucketlistReview> bucketlistReviewList = new ArrayList<>();

    public void update(String content) {
        post.modifiedContent(content);
    }
}

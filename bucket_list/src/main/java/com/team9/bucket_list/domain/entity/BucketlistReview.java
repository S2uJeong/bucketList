package com.team9.bucket_list.domain.entity;

import jakarta.persistence.*;

@Entity
public class BucketlistReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucketlist_review_id")
    private Long id;

    private String content;
    private Long writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucketlist_id")
    private Bucketlist bucketlist;
}

package com.team9.bucket_list.domain.entity;

import jakarta.persistence.*;

@Entity
public class MemberBucketlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_bucketlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucketlist_id")
    private Bucketlist bucketlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}

package com.team9.bucket_list.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BucketlistReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucketlist_review_id")
    private Long id;

    private String content;
    private Long writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bucketlist_id")
    private Bucketlist bucketlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public void update(String content) {
        this.content = content;
    }
}

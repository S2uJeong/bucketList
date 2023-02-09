package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewDto;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
import com.team9.bucket_list.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketlistReviewService {

    private final BucketlistReviewRepository bucketlistReviewRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 유효성 검사
    public Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    public Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
    }

    public BucketlistReview checkBucketlistReview(Long reviewId) {
        return bucketlistReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.REVIEW_NOT_FOUND));
    }


    // list, create, update, delete
    public Page<BucketlistReview> list(Long postId, Pageable pageable) {

        Post post = checkPost(postId);

        return bucketlistReviewRepository.findAllByPost(post, pageable);
    }

    public String create(Long memberId, BucketlistReviewRequest bucketlistReviewRequest) {

        Post post = checkPost(bucketlistReviewRequest.getTargetPostId());
        Member member = checkMember(memberId);

        Optional<BucketlistReview> bucket = bucketlistReviewRepository.findByWriterIdAndPost_Id(memberId, bucketlistReviewRequest.getTargetPostId());
        if(bucket.isPresent()){
//            throw new ApplicationException(ErrorCode.DUPLICATED_REVIEW);
            return "duplicated";
        }

        bucketlistReviewRepository.save(bucketlistReviewRequest.toEntity(post, member));

        return "true";
    }
}


//    // 리뷰 상세 확인
//    public BucketlistReviewDto detail(Long postId, Long ratingId, Pageable pageable) {
//
//        // 포스트 존재 여부 확인
//        Bucketlist bucketlist = bucketlistRepository.findById(postId)
//                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
//
//        // 리뷰 존재 여부 확인
//        BucketlistReview bucketlistReview = bucketlistReviewRepository.findById(ratingId)
//                                                    // 리뷰 에러코드를 생성하여 변경 할 예정
//                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
//
//        return BucketlistReviewDto.builder()
//                .id(bucketlistReview.getId())
//                .writerId(bucketlistReview.getWriterId())
//                .content(bucketlistReview.getContent())
//                .build();
//    }

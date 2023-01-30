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

@Service
@RequiredArgsConstructor
public class BucketlistReviewService {

    private final BucketlistReviewRepository bucketlistReviewRepository;
    private final BucketlistRepository bucketlistRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

    // 유효성 검사
    public Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    public Member checkMember(String userName) {
        return memberRepository.findByUserName(userName)
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

    public String create(Long postId, String userName, BucketlistReviewRequest bucketlistReviewRequest) {

        Post post = checkPost(postId);
        Member member = checkMember(userName);
        Bucketlist bucketlist = bucketlistRepository.findByPostId(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.BUCKETLIST_NOT_FOUND));

        if (!bucketlist.getMemberBucketlistList().contains(userName)) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

//        BucketlistReview bucketlistReview = bucketlistReviewRepository.save(bucketlistReviewRequest.toEntity(member, bucketlist));
        bucketlistReviewRepository.save(bucketlistReviewRequest.toEntity(member, bucketlist));

        alarmRepository.save(Alarm.of(member, post.getTitle()+"에 대한 리뷰가 작성 되었습니다."));

        return "true";
    }

    @Transactional
    public String update(Long postId, Long reviewId, String userName, BucketlistReviewRequest bucketlistReviewRequest) {

        Post post = checkPost(postId);
        BucketlistReview bucketlistReview = checkBucketlistReview(reviewId);
        Member member = checkMember(userName);

        if (bucketlistReview.getWriterId() != member.getId()) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        bucketlistReview.update(bucketlistReviewRequest.getContent());
        bucketlistReviewRepository.save(bucketlistReview);

        alarmRepository.save(Alarm.of(member, member.getUserName()+"에 대한 리뷰가 수정 되었습니다."));

        return "true";
    }

    @Transactional
    public String delete(Long postId, Long reviewId, String userName) {

        Post post = checkPost(postId);
        BucketlistReview bucketlistReview = checkBucketlistReview(reviewId);
        Member member = checkMember(userName);

        if (bucketlistReview.getWriterId() != member.getId()) {
            throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
        }

//        bucketlistRepository.deleteById(postId);
        bucketlistReviewRepository.deleteById(postId);

        alarmRepository.save(Alarm.of(member, member.getUserName()+"에 대한 리뷰가 삭제 되었습니다."));

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

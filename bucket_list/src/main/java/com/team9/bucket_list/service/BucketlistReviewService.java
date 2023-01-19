package com.team9.bucket_list.service;

import com.finalproject_sujeongchoi_team9.exception.ErrorCode;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewDto;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.entity.Bucketlist;
import com.team9.bucket_list.domain.entity.BucketlistReview;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.exception.AppException;
import com.team9.bucket_list.exception.ErrorCode;
import com.team9.bucket_list.execption.AppException;
import com.team9.bucket_list.repository.BucketlistRepository;
import com.team9.bucket_list.repository.BucketlistReviewRepository;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BucketlistReviewService {

    private final BucketlistReviewRepository bucketlistReviewRepository;
    private final PostRepository postRepository;
    private final BucketlistRepository bucketlistRepository;
    private final MemberRepository memberRepository;

    // bucketlist 리뷰 출력
    public Page<BucketlistReview> ratingList(Long postId, Pageable pageable) {

        // 포스트 존재 여부 확인
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트를 확인할 수 없습니다."));
                                                    // bucketlistReviewRepository에 findAllBucketlist 추가 할 예정
        return bucketlistReviewRepository.findAllBucketlist(bucketlist, pageable);
    }

    // 리뷰 상세 확인
    public BucketlistReviewDto detail(Long postId, Long ratingId, Pageable pageable) {

        // 포스트 존재 여부 확인
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트를 확인할 수 없습니다."));

        // 리뷰 존재 여부 확인
        BucketlistReview bucketlistReview = bucketlistReviewRepository.findById(ratingId)
                                                    // 리뷰 에러코드를 생성하여 변경 할 예정
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 리뷰를 확인할 수 없습니다."));

        return BucketlistReviewDto.builder()
                .id(bucketlistReview.getId())
                .writerId(bucketlistReview.getWriterId())
                .content(bucketlistReview.getContent())
                .build();
    }

    // 리뷰 작성
    public BucketlistReviewDto create(Long postId, BucketlistReviewRequest bucketlistReviewRequest) {

        // 포스트 존재 여부 확인
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트를 확인할 수 없습니다."));

        BucketlistReview bucketlistReview = bucketlistReviewRepository.save(bucketlistReviewRequest.toEntity(bucketlist));

//                return BucketlistReviewDto.builder()
//                .writerId(bucketlistReview.getWriterId())
//                .content(bucketlistReview.getContent())
//                .build();

        return BucketlistReviewDto.builder()
                .id(bucketlistReview.getId())
                .writerId(bucketlistReview.getWirterId())
                .content(bucketlistReview.getContent())
                .build();
    }

    @Transactional
    // 리뷰 수정이 필요한가?
    public String update(Long postId, Long userId, BucketlistReviewRequest bucketlistReviewRequest) {

        // 포스트 존재 여부 확인
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트를 확인할 수 없습니다."));

        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUNDED, "해당 멤버를 확인할 수 없습니다."));

        if (bucketlist.getPost().getMember().getUserName() != member.userName()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다.");
        }

        bucketlist.update(bucketlistReviewRequest.getContent());
        bucketlistRepository.save(bucketlist);

        return "수정이 완료 되었습니다.";
    }

    @Transactional
    public String delete(Long postId, Long userId) {

        // 포스트 존재 여부 확인
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 포스트를 확인할 수 없습니다."));

        // 멤버 존재 여부 확인
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUNDED, "해당 멤버를 확인할 수 없습니다."));

        if (bucketlist.getPost().getMember().getNickname() != member.getNickname()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다.");
        }

        bucketlistRepository.deleteById(postId);

        return "글 삭제가 완료되었습니다.";
    }
}

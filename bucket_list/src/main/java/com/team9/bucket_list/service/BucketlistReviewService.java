package com.team9.bucket_list.service;

import com.finalproject_sujeongchoi_team9.exception.ErrorCode;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.entity.Bucketlist;
import com.team9.bucket_list.domain.entity.BucketlistReview;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewDto;
import com.team9.bucket_list.execption.AppException;
import com.team9.bucket_list.repository.BucketlistRepository;
import com.team9.bucket_list.repository.BucketlistReviewRepository;
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

    // 수정 필요
    public Page<BucketlistReviewDto> ratingList(Long postId, Pageable pageable) {

        // 이걸 어디에 써야하지?
        Post post = postRepository.findById(postId)
                .orElseThrow();

        Page<BucketlistReview> bucketlistReviews = bucketlistReviewRepository.findAll(pageable);
        Page<BucketlistReviewDto> bucketlistReviewDtos = BucketlistReviewDto.BucketlistReviewDto(bucketlistReviews);
        return bucketlistReviewDtos;
    }

    // 미완
    public BucketlistReviewDto detail(Long postId) {
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow();

        return BucketlistReviewDto.builder()

                .build();
    }

    public String create(Long postId, BucketlistReviewRequest bucketlistReviewRequest) {

        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow();

        BucketlistReview bucketlistReview = bucketlistReviewRepository.save(bucketlistReviewRequest.toEntity(bucketlist));
//        return BucketlistReviewDto.builder()
//                .writerId(bucketlistReview.getWriterId())
//                .content(bucketlistReview.getContent())
//                .build();

        return "글 작성이 완료 되었습니다.";
    }

    @Transactional
    public String update(Long postId, Long userId, BucketlistReviewRequest bucketlistReviewRequest) {
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow();

        Member member = memberRepository.findById(userId)
                .orElseThrow();

        if (bucketlist.getPost().getMember().getNickname() != member.getNickname()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다.");
        }

        bucketlist.update(bucketlistReviewRequest.getContent());
        bucketlistRepository.save(bucketlist);

        return "수정이 완료 되었습니다.";
    }

    @Transactional
    // 삭제는 신청해서 진행.. 수정 필요
    public String delete(Long postId, Long userId) {
        Bucketlist bucketlist = bucketlistRepository.findById(postId)
                .orElseThrow();

        Member member = memberRepository.findById(userId)
                .orElseThrow();

        if (bucketlist.getPost().getMember().getNickname() != member.getNickname()) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다.");
        }

        bucketlistRepository.deleteById(postId);

        return "글 삭제가 완료되었습니다.";
    }
}

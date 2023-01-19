package com.team9.bucket_list.service;

import com.finalproject_sujeongchoi_team9.exception.ErrorCode;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.AppException;
import com.team9.bucket_list.repository.MemberRepository;
import com.team9.bucket_list.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // ========= 유효성검사 ===========
    // 1. findByMemberId : memberId로 member 찾아 로그인 되어있는지 확인 ->  error : 권한이 없습니다.
    public Member checkMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUNDED));
    }
    // 2. checkPostMember : post를 작성한 mem과 현재 로그인 된 mem 비교 -> INVALID_PERMISSION
    public void checkPostMember(Long memberId, Long postMemberId) {
        Member loginMember = memberRepository.findAllById(memberId);
        Member postMember = memberRepository.findAllById(postMemberId);
        if(!loginMember.equals(postMember)) throw new AppException(ErrorCode.INVALID_PERMISSION);
    }
    // 3. findByPostId : postId에 따른 post가 DB에 잘 있는지 확인 -> error : 없는 게시물입니다. POST_NOT_FOUND
    public Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
    }


    // 작성
    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request, Long memberId) {

        return null;
    }

    // 전체 조회 (page)
    public Page<PostReadResponse> findAllPost(Pageable pageable) {

        return null;
    }

    // 상세조회
    public PostReadResponse findPost(Long id) {

        return null;
    }

    // 내가 쓴 글만 조회 (마이피드)
    public Page<PostReadResponse> findMyPost(Long userId, Pageable pageable) {

        return null;
    }

    // 수정
    @Transactional
    public PostUpdateResponse update(Long userId, PostUpdateRequest request) {

        return null;
    }

    // 삭제
    @Transactional
    public PostDeleteResponse delete(Long id, String userName) {

        return null;

    }

}


package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.execption.ApplicationException;
import com.team9.bucket_list.execption.ErrorCode;
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
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUNDED));
    }
    // 2. checkPostMember : post를 작성한 mem과 현재 로그인 된 mem 비교 -> INVALID_PERMISSION
    public void checkPostMember(Long memberId, Long postMemberId) {
        Member loginMember = checkMember(memberId);
        Member postMember = checkMember(postMemberId);
        if(!loginMember.equals(postMember)) throw new ApplicationException(ErrorCode.INVALID_PERMISSION);
    }
    // 3. findByPostId : postId에 따른 post가 DB에 잘 있는지 확인 -> error : 없는 게시물입니다. POST_NOT_FOUND
    public Post checkPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    // 작성
    @Transactional
    public PostCreateResponse create(PostCreateRequest request, Long memberId) {
        // 로그인 되어있는지 확인하고 아니면 에러던짐
        Member member = checkMember(memberId);
        // requestDTO 안의 toEntity 메서드를 이용해 post Entity 객체를 생성한다.
        Post post = request.toEntity(member);
        // request를 통해 만들어진 post를 DB에 저장한다.
        Post savedPost = postRepository.save(post);
        return PostCreateResponse.of(savedPost);
    }

    // 전체 조회 (page)
    public Page<PostReadResponse> readAll(Pageable pageable) {
        // Entity
        Page<Post> posts = postRepository.findAll(pageable);
        // Dto
        Page<PostReadResponse> postReadResponses = PostReadResponse.listOf(posts);
        return postReadResponses;
    }

    // 상세조회
    public PostReadResponse read(Long postId) {
        // Entity
        Post post = checkPost(postId);
        // Dto
        PostReadResponse postReadResponse = PostReadResponse.detailOf(post);
        return postReadResponse;
    }

    // 내가 쓴 글만 조회 (마이피드)
    // return을 뭘로 해야될지 모르겠음.. 나중에 마이페이지 생길 때까지 보류
    public Page<PostReadResponse> findMine(Long memberId, Pageable pageable) {
        return null;
    }

    // 수정
    @Transactional
    public PostUpdateResponse update(PostUpdateRequest request, Long memberId, Long postId) {
        // 로그인 되어있는지 확인하고 아니면 에러던짐
        Member member = checkMember(memberId);
        // postid에 해당하는 post가 DB에 없으면 에러던짐
        Post post = checkPost(postId);
        // post를 쓴 멤버와 로그인 되어 있는 member가 같은 멤버가 아니면 에러던짐
        checkPostMember(memberId, post.getId());
        /*post.setTitle(request.getTitle());
        request로 들어온 내용으로 post 안의 속성들을 set해줌으로써 DB에 저장시키고, Dto로 반환하고 시픙데,, 어떻게 하지*/
        return null;
    }

    // 삭제
    @Transactional
    public PostDeleteResponse delete(Long postId, Long memberId) {
        // 로그인 되어있는지 확인하고 아니면 에러던짐
        Member member = checkMember(memberId);
        // postid에 해당하는 post가 DB에 없으면 에러던짐
        Post post = checkPost(postId);
        // post를 쓴 멤버와 로그인 되어 있는 member가 같은 멤버가 아니면 에러던짐
        checkPostMember(memberId, post.getId());

        postRepository.deleteById(post.getId());
        return PostDeleteResponse.of(post);

    }

}


package com.team9.bucket_list.service;

import com.team9.bucket_list.domain.dto.post.*;
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

    // 작성
    @Transactional
    public PostCreateResponse createPost(PostCreateRequest request, Long userId) {

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


package com.team9.bucket_list.controller.rest;
import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    //== 작성 ==//
    @PostMapping
    // 여기서 Member는, 로그인 된 member를 뜻한다.
    public Response<PostCreateResponse> createPost(@RequestBody PostCreateRequest request, Long memberId) {
        PostCreateResponse response = postService.create(request,memberId);
        log.info("Post 작성 성공 postId: {}", response.getPostId());
        return Response.success(response);
    }

    //== 전체조회 ==//
    @GetMapping("list")
    public Response<Page<PostReadResponse>> readAllPost(@PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC)
                                                        Pageable pageable) {
        Page<PostReadResponse> postReadResponses = postService.readAll(pageable);
        log.info("PostList 보기 성공");
        return Response.success(postReadResponses);
    }

    //== 세부조회 ==//
    @GetMapping("/{postId}")
    public Response<PostReadResponse> readPost(@PathVariable("postId") long postId) {
        PostReadResponse postReadResponse = postService.read(postId);
        log.info("Post 보기 성공");
        return Response.success(postReadResponse);
    }

    //== 수정 ==//  ---> 진행중
    @PutMapping("/{postId}/{memberId}")
    public Response<PostUpdateResponse> updatePost() {
        return null;
    }

    //== 삭제 ==//
    @DeleteMapping("/{postId}/{memberId}")
    public Response<PostDeleteResponse> deletePost(
            @PathVariable("postId") long postId, @PathVariable("memberId") long memberId ) {
        PostDeleteResponse postDeleteResponse = postService.delete(postId, memberId);
        log.info("Post 삭제 성공");
        return Response.success(postDeleteResponse);
    }


}

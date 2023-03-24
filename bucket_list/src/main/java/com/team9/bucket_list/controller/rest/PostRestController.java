package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewRequest;
import com.team9.bucket_list.domain.dto.bucketlistReview.BucketlistReviewResponse;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.service.BucketlistReviewService;
import com.team9.bucket_list.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "버킷리스트(게시글)", description = "게시글 CRUD 기능을 수행합니다. 좋아요, 신청서 수락/거절 기능을 포함합니다.")
public class PostRestController {

    private final PostService postService;
    private final BucketlistReviewService bucketlistReviewService;

    // 게시글 폼에서 데이터 받아오기(Ajax 사용하여 받아옴)
    @PostMapping(value = "/detailpost" ,produces = "application/json")
    @ResponseBody
    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    public Response<PostIdResponse> getData(@RequestBody PostCreateRequest request,Authentication authentication){
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;

        log.info("detailpost");
        String userName = "test";
        PostCreateResponse response = postService.create(request,userId);       // DB에 데이터 저장
        log.info("postId():"+response.getPostId());
        PostIdResponse postid = new PostIdResponse(response.getPostId());
        return Response.success(postid);
    }


        //== 검색 기능 ==//
    // 검색 데이터 전송하고 반환
    @GetMapping("/search/list")
    @ResponseBody
    @Operation(summary = "검색 기능", description = "카테고리, 날짜, 키워드를 입력받아 검색합니다.")
    public Response<Page<PostReadResponse>> searchPost(@Parameter(hidden = true) @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                       @RequestParam(value = "category", required = false) String category, @RequestParam(value = "keyword", required = false) String keyword
            , @RequestParam(value = "eventStart", required = false) String eventStart, @RequestParam(value = "eventEnd", required = false) String eventEnd
            , @RequestParam(value = "lowCost", required = false) String lowCost, @RequestParam(value = "upCost", required = false) String upCost) {

        log.info(lowCost);
        log.info(upCost);

        Page<PostReadResponse> response = postService.search(pageable,category,keyword,eventStart,eventEnd,lowCost,upCost);
        return Response.success(response);
    }

    //== 전체조회 ==//
    @GetMapping("/list")
    @ResponseBody
    @Operation(summary = "게시글 조회", description = "카테고리 별로 게시글 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> readAllPost(@Parameter(hidden = true) @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                        @RequestParam(value = "category", required = false) String category,
             @RequestParam(value = "eventStart", required = false) String eventStart, @RequestParam(value = "eventEnd", required = false) String eventEnd
            , @RequestParam(value = "lowCost", required = false) String lowCost, @RequestParam(value = "upCost", required = false) String upCost) {

        //        if(category == null){
//            Page<PostReadResponse> postReadResponses = postService.readAll(pageable);
//            log.info("PostList 보기 성공");
//            return Response.success(postReadResponses);
//        } else{
//            Page<PostReadResponse> filterPosts = postService.filter(category, pageable);
//            return Response.success(filterPosts);
//        }
        Page<PostReadResponse> response = postService.postList(pageable,category,eventStart,eventEnd,lowCost,upCost);
        return Response.success(response);
    }


    @GetMapping(value = "/{postId}/json", produces = "application/json")
    @ResponseBody
    @Operation(summary = "특정 게시글 조회", description = "게시글 id를 통해 조회하여 게시글을 출력합니다.")
    public Response<PostReadResponse> jsonreadPost(@Parameter(name = "postId", description = "게시글 id") @PathVariable(value = "postId") Long postId){
        PostReadResponse postReadResponse = postService.read(postId);
        log.info("DB에서 데이터 호출 location :"+postReadResponse.getLocation());
        return Response.success(postReadResponse);
    }

    // 클라이언트에서 데이터 받아와서 수정
    @PutMapping(value = "/{postId}/update" ,produces = "application/json")
    public Response<PostUpdateResponse> updatePost( @PathVariable Long postId, @RequestBody PostUpdateRequest request,Authentication authentication)  {
        // update 메서드를 통해 request 내용대로 수정해준다. 반환값 : post Entity
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;
        log.info(request.toString());
        log.info("postId:"+postId);
        PostUpdateResponse response = postService.update(request,postId,userId);
        log.info("🔵 Post 수정 성공");
        return Response.success(response);
    }

    //== 삭제 ==//
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "postId에 따라 게시글을 삭제합니다.")
    public Response<String> deletePost(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId,Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;
        postService.delete(postId,userId);
        log.info("Post 삭제 성공");
        return Response.success("삭제 완료");
    }


    //== 좋아요 확인 ==//
    @GetMapping("/{postId}/likes")
    @Operation(summary = "좋아요 - 좋아요 여부 확인", description = "로그인 되어 있는 member가 해당 게시글에 좋아요를 눌렀는지 확인합니다. UI에서 좋아요 버튼이 눌려있는것을 표현하기 위해 구현했습니다.")
    public Response<Integer> checkLike(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId,
                                       Authentication authentication) {
        int checkLike = postService.checkLike(postId, Long.valueOf(authentication.getName()));
        return Response.success(checkLike);
    }

    //== 좋아요 개수 ==//
    @GetMapping("/{postId}/likes/count")
    @Operation(summary = "좋아요 - 좋아요 갯수 확인", description = "해당 게시글에 눌린 좋아요 개수를 출력합니다.")
    public Response<Long> countLike(@Parameter(name = "postId", description = "게시글 id") @PathVariable Long postId) {
        Long cntLike = postService.countLike(postId);
        return Response.success(cntLike);
    }

    //== 좋아요 누르기 ==//
    @PostMapping("/{postId}/likes")
    @Operation(summary = "좋아요 - 좋아요 작성", description = "해당 게시글에 좋아요를 작성합니다(누릅니다).")
    public Response<Integer> clickLike(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId,
                                       Authentication authentication) {
        int result = postService.clickLike(postId, Long.valueOf(authentication.getName()));
        return Response.success(result);
    }

    // 버킷리스트 리뷰
    @GetMapping("/{postId}/reviews")
    @Operation(summary = "리뷰 조회", description = "특정게시글의 리뷰를 pageable하여 출력합니다.")
    public Response<Page<BucketlistReviewResponse>> reviewList(@Parameter(name = "postId", description = "게시글 id")  @PathVariable Long postId,
                                                               @Parameter(hidden = true) @PageableDefault(sort = "createdAt", size = 4, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BucketlistReviewResponse> bucketlistReviewResponses =  bucketlistReviewService.list(postId, pageable);
        return Response.success(bucketlistReviewResponses);
    }

    @PostMapping("/reviews")
    @Operation(summary = "리뷰 작성", description = "특정게시글에 리뷰를 작성합니다.")
    public Response<String> reviewCreate(Authentication authentication, @RequestBody BucketlistReviewRequest bucketlistReviewRequest) {
        Long memberId = Long.valueOf(authentication.getName());
        String result = bucketlistReviewService.create(memberId, bucketlistReviewRequest);
        return Response.success(result);
    }
}

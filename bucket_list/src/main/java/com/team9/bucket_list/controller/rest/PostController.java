package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.post.*;
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
@RequestMapping("/post")
@Tag(name = "버킷리스트(게시글)", description = "게시글 CRUD 기능을 수행합니다. 좋아요, 신청서 수락/거절 기능을 포함합니다.")
public class PostController {

    private final PostService postService;

    // 게시글 폼에서 데이터 받아오기(Ajax 사용하여 받아옴)
    @PostMapping(value = "/detailpost" ,produces = "application/json")
    @ResponseBody
    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    public Response<PostIdResponse> getData(@RequestBody PostCreateRequest request){
//        Long userId = Long.valueOf(authentication.getName());
        Long userId = 1l;

        log.info("detailpost");
        String userName = "test";
        PostCreateResponse response = postService.create(request,userId);       // DB에 데이터 저장
        log.info("postId():"+response.getPostId());
        PostIdResponse postid = new PostIdResponse(response.getPostId());
        return Response.success(postid);
    }

    //== 전체조회 ==//
    @GetMapping("/list")
    @ResponseBody
    @Operation(summary = "게시글 조회", description = "카테고리 별로 게시글 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> readAllPost(@Parameter(hidden = true) @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                        @RequestParam(value = "category", required = false) String category) {
        if(category == null){
            Page<PostReadResponse> postReadResponses = postService.readAll(pageable);
            log.info("PostList 보기 성공");
            return Response.success(postReadResponses);
        } else{
            Page<PostReadResponse> filterPosts = postService.filter(category, pageable);
            return Response.success(filterPosts);
        }
    }

    // 버킷리스트 필터
//    @GetMapping("list")
//    public Response<Page<PostReadResponse>> postFilter(@PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC)
//                                                       Pageable pageable, @RequestParam("category") String category ) {
//        Page<PostReadResponse> filterPosts = postService.filter(category);
//        return Response.success(filterPosts);
//    }

    //== model 사용 세부조회 ==//

//    @GetMapping("/{postId}")
//    public String readPost(@PathVariable(value = "postId") Long postId, Model model){
//        PostReadResponse postReadResponse = postService.read(postId);
//
//        String title = postReadResponse.getTitle();
//        String untile = postReadResponse.getUntilRecruit();
//        String eventStart = postReadResponse.getEventStart();
//        String eventEnd = postReadResponse.getEventEnd();
//        int cost = postReadResponse.getCost();
//        int entrantNum = postReadResponse.getEntrantNum();
//        String category = postReadResponse.getCategory();
//        String location = postReadResponse.getLocation();
//        String content = postReadResponse.getContent();
//        double lat = postReadResponse.getLat();
//        double lng = postReadResponse.getLng();
//
//
//        model.addAttribute("title",title);
//        model.addAttribute("untilRecruit",untile);
//        model.addAttribute("eventStart",eventStart);
//        model.addAttribute("eventEnd",eventEnd);
//        model.addAttribute("cost",cost);
//        model.addAttribute("entrantNum",entrantNum);
//        model.addAttribute("category",category);
//        model.addAttribute("content",content);
//        model.addAttribute("lat",lat);
//        model.addAttribute("lng",lng);
//        model.addAttribute("location",location);
//        model.addAttribute("apikey",postReadResponse.getAPI_KEY());
//        return "Post/PostDetail";
//    }



    @GetMapping(value = "/{postId}/json", produces = "application/json")
    @ResponseBody
    @Operation(summary = "특정 게시글 조회", description = "게시글 id를 통해 조회하여 게시글을 출력합니다.")
    public Response<PostReadResponse> jsonreadPost(@Parameter(name = "postId", description = "게시글 id") @PathVariable(value = "postId") Long postId){
        PostReadResponse postReadResponse = postService.read(postId);
        log.info("DB에서 데이터 호출 location :"+postReadResponse.getLocation());
        return Response.success(postReadResponse);
    }

    //== 삭제 ==//
    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "postId에 따라 게시글을 삭제합니다.")
    public Response<PostDeleteResponse> deletePost(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId ,
                                                   Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());

        PostDeleteResponse postDeleteResponse = postService.delete(postId,userId);
        log.info("Post 삭제 성공");
        return Response.success(postDeleteResponse);
    }


    //== 좋아요 확인 ==//
    @GetMapping("/{postId}/likes/check")
    @Operation(summary = "좋아요 - 좋아요 여부 확인", description = "로그인 되어 있는 member가 해당 게시글에 좋아요를 눌렀는지 확인합니다. UI에서 좋아요 버튼이 눌려있는것을 표현하기 위해 구현했습니다.")
    public Response<Integer> checkLike(@Parameter(name = "postId", description = "게시글 id") @PathVariable("postId") long postId,
                                       Authentication authentication) {
        int checkLike = postService.checkLike(postId, Long.valueOf(authentication.getName()));
        return Response.success(checkLike);
    }

    //== 좋아요 개수 ==//
    @GetMapping("/{postId}/likes")
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

    //== 마이피드 ==//
    // 좋아요한
    @GetMapping("/my/likes")
    @Operation(summary = "마이피드 - 좋아요한 글 조회", description = "로그인 되어 있는 멤버가 좋아요한 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedLike(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                       Authentication authentication) {
        Page<PostReadResponse> likePosts = postService.myFeedLike(pageable, Long.valueOf(authentication.getName()));
        return Response.success(likePosts);
    }

    // 작성한
    @GetMapping("/my/create")
    @Operation(summary = "마이피드 - 작성한 글 조회", description = "로그인 되어 있는 멤버가 작성한 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedCreate(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                         Authentication authentication) {
        Page<PostReadResponse> createPosts = postService.myFeedCreate(pageable, Long.valueOf(authentication.getName()));
        return Response.success(createPosts);
    }

    // 신청한
    @GetMapping("/my/apply")
    @Operation(summary = "마이피드 - 참가 신청한 글 조회", description = "로그인 되어 있는 멤버가 참가 신청한 Post 리스트를 출력합니다.")
    public Response<Page<PostReadResponse>> myFeedApply(@Parameter(hidden = true) @PageableDefault(size = 16, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                        Authentication authentication) {
        Page<PostReadResponse> applyPosts = postService.myFeedApply(pageable, Long.valueOf(authentication.getName()));
        return Response.success(applyPosts);
    }
}

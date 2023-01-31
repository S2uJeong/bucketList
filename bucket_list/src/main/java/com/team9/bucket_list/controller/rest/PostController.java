package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    @Value("${google.map.key}")
    private Object API_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!


    private final PostService postService;

    // 게시글 작성 폼 페이지 이동
    @GetMapping("/createform")
    public String movePostForm(){
        return "Post/PostCreate";
    }


    // 게시글 폼에서 데이터 받아오기(Ajax 사용하여 받아옴)
    @PostMapping(value = "/detailpost" ,produces = "application/json")
    @ResponseBody
    public Long getData(@RequestBody PostCreateRequest request) throws UnsupportedEncodingException {
        String userName = "test";

        PostCreateResponse response = postService.create(request,userName);       // DB에 데이터 저장
        return response.getPostId();
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

    @GetMapping("{postId}")
    public String showMap2(@PathVariable(value = "postId") Long postId, Model model){
        PostReadResponse postReadResponse = postService.read(postId);

        String title = postReadResponse.getTitle();
        String untile = postReadResponse.getUntilRecruit();
        String eventStart = postReadResponse.getEventStart();
        String eventEnd = postReadResponse.getEventEnd();
        int cost = postReadResponse.getCost();
        int entrantNum = postReadResponse.getEntrantNum();
        String category = postReadResponse.getCategory();
        String location = postReadResponse.getLocation();
        String content = postReadResponse.getContent();
        double lat = postReadResponse.getLat();
        double lng = postReadResponse.getLng();


        model.addAttribute("title",title);
        model.addAttribute("untilRecruit",untile);
        model.addAttribute("eventStart",eventStart);
        model.addAttribute("eventEnd",eventEnd);
        model.addAttribute("cost",cost);
        model.addAttribute("entrantNum",entrantNum);
        model.addAttribute("category",category);
        model.addAttribute("content",content);
        model.addAttribute("lat",lat);
        model.addAttribute("lng",lng);
        model.addAttribute("location",location);
        model.addAttribute("apikey",API_KEY);
        return "Post/PostDetail";
    }


    //== 수정 ==//  ---> 진행중 fetchMapping 사용 고려
    @PutMapping("/update")
    public String updatePost() {
        return "Post/PostUpdate";
    }

    //== 삭제 ==//
    @DeleteMapping("/{postId}/{memberId}")
    public Response<PostDeleteResponse> deletePost(
            @PathVariable("postId") long postId, @PathVariable("memberId") long memberId ) {
        PostDeleteResponse postDeleteResponse = postService.delete(postId, memberId);
        log.info("Post 삭제 성공");
        return Response.success(postDeleteResponse);
    }

    //== 좋아요 개수 ==//
    @GetMapping("/{postId}/likes")
    public Response<Long> countLike(@PathVariable Long postId) {
        Long cntLike = postService.countLike(postId);
        return Response.success(cntLike);
    }

    //== 좋아요 누르기 ==//
    @PostMapping("/{postId}/likes")
    public Response<Integer> clickLike(@PathVariable("postId") long postId, Authentication authentication) {
        int result = postService.clickLike(postId, authentication.getName());
        return Response.success(result);
    }

    //== 마이피드 ==//
    @GetMapping("/my")
    public Response<MyFeedResponse> myFeed(@PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC)
                                           Pageable pageable, Authentication authentication) {
        Page<PostReadResponse> createPosts = postService.myFeedCreate(pageable, authentication.getName());
        Page<PostReadResponse> applyPosts = postService.myFeedApply(pageable, authentication.getName());
        Page<PostReadResponse> likePosts = postService.myFeedLike(pageable, authentication.getName());
        return Response.success(new MyFeedResponse(createPosts, applyPosts, likePosts));
    }
}


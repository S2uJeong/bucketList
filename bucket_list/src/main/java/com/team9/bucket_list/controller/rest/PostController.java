package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.post.*;
import com.team9.bucket_list.domain.entity.Post;
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
    public String readPost(@PathVariable(value = "postId") Long postId, Model model){
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


    //== 수정 ==// ==> (리팩토링) rest 형식 또 만들 예정
     // 게시글 수정 폼 페이지 이동
    @GetMapping("{postId}/edit")
    public String updateForm(@PathVariable("postId") Long postId, Model model){
        // 수정을 요청한 postId의 post가 유효한지 검사
        Post post = postService.checkPost(postId);
        // 이전 게시글을 불러온다.
        PostUpdateResponse postUpdateResponse = PostUpdateResponse.prePost(post);
        // model 파라미터를 통해 이전에 작성된 post의 내용을 뷰로 전달한다.
        model.addAttribute("prePost", postUpdateResponse);
        return "Post/PostUpdateForm";
    }

     // 클라이언트에서 데이터 받아와서 수정
    @PostMapping("/{postId}/edit")
    public String updatePost( @PathVariable Long postId, @ModelAttribute("updateDto") PostUpdateRequest request)  {
        // update 메서드를 통해 request 내용대로 수정해준다. 반환값 : post Entity
        postService.update(request,postId);
        log.info("🔵 Post 수정 성공");
        return "Post/success"; // post 상세 조회 화면으로 연결할 예정. 임시 html 연결함.
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


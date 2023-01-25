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


    //=== 작성 ====//
    private final PostService postService;

      // 게시글 작성 폼 페이지 이동
    @GetMapping("/createForm")
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


    //== 수정 ==//
    // form 으로 활용해보기 도전해 볼까?
    /*@GetMapping("/updateForm/")
    public String updatePostForm(Long postId, Model model){
        Post post = postService.checkPost(postId);
        PostUpdateRequest request = new PostUpdateRequest();

        return "Post/PostUpdate";
    }*/

    @PutMapping("/{postId}")
    public Response<PostUpdateResponse> updatePost( @RequestBody PostUpdateRequest request, @PathVariable("postId") Long postId)  {
        PostUpdateResponse postUpdateResponse =  postService.update(request,postId);
        log.info("Post 수정 성공");
        return Response.success(postUpdateResponse);
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


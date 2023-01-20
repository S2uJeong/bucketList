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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    // 게시글 작성 폼 페이지 이동
    @GetMapping("/createform")
    public String location(){
        return "PostCreate";
    }

    //AJAX 데이터 받아오기
    @PostMapping(value="/save", produces = "application/json")
    public String getPostData(@RequestBody Map<String,Object> objectMap
    ) {
        String address = (String) objectMap.get("address");// 프론트에서 저장할 때 adress key 안에 주소값을 넣어둠
        PostCreateRequest postCreateRequest = new PostCreateRequest().test(address);
        postService.create(postCreateRequest, 1L);
        // String title = (String) objectMap.get("title");  // 이렇게 하면 가독성 나쁘니까 dto 단에서 스트림으로 만들어준다 key:val -> postCreateRequest에서 제작예정
        // Post에 위치 저장시키는 Service 기능 추가
        log.info("ajax return");
        return "forward:/post/map";
    }

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

    // 게시물 데이터 출력(세부 게시물 데이터 출력)
    @PostMapping("/map")
    public String viewData(Model model) {
        System.out.println("map 페이지");
        PostReadResponse postReadResponse = postService.read(1L); // postId 임의 숫자 넣음
        double lat = postReadResponse.getLat();
        double lng = postReadResponse.getLng();
        log.info("lat : []", lat);
        log.info("lng : []", lng);
        String location = postReadResponse.getLocation();
        log.info("ldcation : []" , location);
        model.addAttribute("lat",lat);
        model.addAttribute("lng",lng);
        model.addAttribute("location",location);
        return "map";  // 이 부분 GetMapping으로 바꾸기 위해 어떻게 해야할 지 고민해봐야 함
    }

    //== 수정 ==//  ---> 진행중 fetchMapping 사용 고려
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

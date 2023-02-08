package com.team9.bucket_list.controller.front;

import com.team9.bucket_list.domain.dto.post.PostUpdateRequest;
import com.team9.bucket_list.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostViewController {

    private final PostService postService;

    // 게시글 작성 폼 페이지 이동
    @GetMapping("/createform")
    public String movePostForm(){
        return "Post/PostCreate";
    }

    //== json 세부조회 ==//

    @GetMapping("/{postId}")        // 페이지 이동만을 위한 코드
    public String readPost(@PathVariable(value = "postId") Long postId){
        log.info("postdetail 페이지 이동");
        return "Post/postDetailUI";
    }

    //== 수정 ==// ==> (리팩토링) rest 형식 또 만들 예정
    // 게시글 수정 폼 페이지 이동
    @GetMapping("{postId}/edit")
    public String updateForm(@PathVariable("postId") Long postId){
        // 수정을 요청한 postId의 post가 유효한지 검사
        postService.checkPost(postId);
        return "Post/PostUpdateUI";
    }



    @GetMapping
    public String postList() { return "post"; }

}

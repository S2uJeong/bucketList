package com.team9.bucket_list.controller.front;

import com.team9.bucket_list.domain.dto.post.PostUpdateRequest;
import com.team9.bucket_list.domain.dto.post.PostUpdateResponse;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
//        return "Post/AxiosPostDetail";
        return "Post/postDetailUI";
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

    @GetMapping
    public String postList() { return "post"; }

}

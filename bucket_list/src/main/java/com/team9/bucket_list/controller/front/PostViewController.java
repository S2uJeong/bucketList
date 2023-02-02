package com.team9.bucket_list.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
public class PostViewController {

    @GetMapping
    public String postList() { return "post"; }

    //== json 세부조회 ==//

    @GetMapping("/{postId}")        // 페이지 이동만을 위한 코드
    public String readPost(){
        return "Post/postDetailUI";
    }
}

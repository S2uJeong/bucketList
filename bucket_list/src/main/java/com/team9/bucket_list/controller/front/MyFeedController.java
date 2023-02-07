package com.team9.bucket_list.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my")
public class MyFeedController {

    @GetMapping
    public String myFeedList() { return "myFeed"; }

    @GetMapping("/likes")
    public String myFeedLikeList() { return "myFeedLike"; }
}

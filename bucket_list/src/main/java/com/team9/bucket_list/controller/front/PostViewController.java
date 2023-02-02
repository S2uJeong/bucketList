package com.team9.bucket_list.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post")
public class PostViewController {

    @GetMapping
    public String postList() { return "post"; }
}

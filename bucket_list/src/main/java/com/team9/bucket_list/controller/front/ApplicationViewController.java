package com.team9.bucket_list.controller.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
@RequiredArgsConstructor
public class ApplicationViewController {

    @GetMapping("/form/{postId}")
    public String addForm() {
        return "applicationForm";
    }

    @GetMapping("/form/{postId}/complete")
    public String addComplete() {
        return "applicationComplete";
    }
}

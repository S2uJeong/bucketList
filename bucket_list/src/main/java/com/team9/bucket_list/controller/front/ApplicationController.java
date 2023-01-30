package com.team9.bucket_list.controller.front;

import com.team9.bucket_list.controller.rest.ApplicationRestController;
import com.team9.bucket_list.domain.dto.application.ApplicationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationRestController applicationRestController;

    @GetMapping("/form/{postId}")
    public String form(@PathVariable Long postId, Model model) {
        model.addAttribute("postId",postId);
        return "form";
    }
}

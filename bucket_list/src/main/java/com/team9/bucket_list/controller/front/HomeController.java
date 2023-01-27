package com.team9.bucket_list.controller.front;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping
public class HomeController {

    @ResponseBody
    @GetMapping("/click")
    public String click(Authentication authentication) {
        log.info("name={}", authentication.getName());
        return "click";
    }

    @GetMapping
    public String home(Authentication authentication, Model model) {
        if (authentication == null) {
            return "index";
        } else {
            model.addAttribute("member", authentication.getName());
            return "loginHome";
        }
    }
}

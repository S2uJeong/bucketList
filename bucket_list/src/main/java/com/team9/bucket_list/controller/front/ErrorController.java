package com.team9.bucket_list.controller.front;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/404ErrorPage")
@RequiredArgsConstructor
@Slf4j
public class ErrorController {

    @GetMapping
    public String errorPage() { return "404"; }
}

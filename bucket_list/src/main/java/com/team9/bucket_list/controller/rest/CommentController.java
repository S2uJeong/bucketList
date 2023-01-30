package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.comment.Create.CommentCreateRequest;
import com.team9.bucket_list.domain.dto.comment.Create.CommentCreateResponse;
import com.team9.bucket_list.domain.dto.comment.Create.CommentListResponse;
import com.team9.bucket_list.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;


    // 댓글 작성
    @PostMapping("/{postsId}")
    @ResponseBody
    public CommentCreateResponse commentCreate(@PathVariable(name = "postsId")Long id,@RequestBody CommentCreateRequest request){
        String userName = "test";
        log.info("댓글작성 username :"+userName);
        return commentService.commentCreate(id,request,userName);
    }

    // 댓글 리스트 전체 출력
    @GetMapping("/{postId}/comments")
    @ResponseBody
    public List<CommentListResponse> commentList(@PathVariable(name = "postId") Long id){
        List<CommentListResponse> commentList = commentService.commentList(id);
        return commentList;
    }

    // 댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    @ResponseBody
    public List<CommentListResponse> commentUpdate(@PathVariable(name = "postId")Long postid, @PathVariable(name="commentId")Long id,@RequestBody CommentCreateRequest request){
        String userName = "test";
        List<CommentListResponse> commentList = commentService.updateComment(postid,id,request,userName);
        return commentList;
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comments/{commentId}")
    @ResponseBody
    public List<CommentListResponse> commentDelete(@PathVariable(name = "postId")Long postid, @PathVariable(name="commentId")Long id){
        String userName = "test";
        List<CommentListResponse> commentList = commentService.deleteComment(postid,id,userName);
        return commentList;
    }
}

package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.comment.CommentCreateRequest;
import com.team9.bucket_list.domain.dto.comment.CommentCreateResponse;
import com.team9.bucket_list.domain.dto.comment.CommentListResponse;
import com.team9.bucket_list.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;


    // 댓글 작성
    @PostMapping("/{postId}")

    public Response<CommentCreateResponse> commentCreate(@PathVariable(name = "postId")Long id, @RequestBody CommentCreateRequest request){
        String userName = "han";
        log.info("댓글작성 username :"+userName);
        return Response.success(commentService.commentCreate(id,request,userName));
    }

    // 댓글 리스트 전체 출력
    @GetMapping("/{postId}/comments")
    public Response<List<CommentListResponse>> commentList(@PathVariable(name = "postId") Long id){
        List<CommentListResponse> commentList = commentService.commentList(id);
        return Response.success(commentList);
    }

    // 댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    public Response<List<CommentListResponse>> commentUpdate(@PathVariable(name = "postId")Long postid, @PathVariable(name="commentId")Long id,@RequestBody CommentCreateRequest request){
        String userName = "han";
        List<CommentListResponse> commentList = commentService.updateComment(postid,id,request,userName);
        return Response.success(commentList);
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/comments/{commentId}")
    @ResponseBody
    public Response<List<CommentListResponse>> commentDelete(@PathVariable(name = "postId")Long postid, @PathVariable(name="commentId")Long id){
        String userName = "han";
        List<CommentListResponse> commentList = commentService.deleteComment(postid,id,userName);
        return Response.success(commentList);
    }
}

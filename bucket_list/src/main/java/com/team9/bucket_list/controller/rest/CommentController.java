package com.team9.bucket_list.controller.rest;

import com.team9.bucket_list.domain.Response;
import com.team9.bucket_list.domain.dto.comment.CommentCreateRequest;
import com.team9.bucket_list.domain.dto.comment.CommentCreateResponse;
import com.team9.bucket_list.domain.dto.comment.CommentListResponse;
import com.team9.bucket_list.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Tag(name = "버킷리스트 댓글", description = "작성한 버킷리스트 게시글에 댓글을 작성할 수 있습니다.")
public class CommentController {

    private final CommentService commentService;


    // 댓글 작성
    @PostMapping("/{postId}")
    @Operation(summary = "댓글 작성", description = "id를 이용하여 user 레코드를 조회합니다.")
    public Response<CommentCreateResponse> commentCreate(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId")Long id,
                                                         @RequestBody CommentCreateRequest request,Authentication authentication){
        Long userId = Long.valueOf(authentication.getName());
//        Long userId = 1l;
        log.info("댓글작성 username :"+userId);
        return Response.success(commentService.commentCreate(id,request,userId));
    }

    // 댓글 리스트 전체 출력
    @GetMapping("/{postId}/list")
    @Operation(summary = "댓글 리스트 조회", description = "특정 게시글의 댓글 리스트를 출력합니다.")
    public Response<List<CommentListResponse>> commentList(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId") Long id){
        List<CommentListResponse> commentList = commentService.commentList(id);
        return Response.success(commentList);
    }

    // 댓글 수정
    @PutMapping("/{postId}/update/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    public Response<List<CommentListResponse>> commentUpdate(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId")Long postid,
                                                             @Parameter(name = "commentId", description = "댓글 id") @PathVariable(name="commentId")Long id,
                                                             @RequestBody CommentCreateRequest request,Authentication authentication){
        Long memberId = Long.valueOf(authentication.getName());
//        Long memberId = 1l;
        List<CommentListResponse> commentList = commentService.updateComment(postid,id,request,memberId);
        return Response.success(commentList);
    }

    // 댓글 삭제
    @DeleteMapping("{postId}/delete/{commentId}")
    @ResponseBody
    public Response<List<CommentListResponse>> commentDelete(@Parameter(name = "postId", description = "게시글 id") @PathVariable(name = "postId")Long postid,
                                                             @Parameter(name = "commentId", description = "댓글 id") @PathVariable(name="commentId")Long id
            ,Authentication authentication){
        log.info("댓글 삭제");
        Long memberId = Long.valueOf(authentication.getName());
//        Long memberId = 1l;
        List<CommentListResponse> commentList = commentService.deleteComment(postid,id,memberId);
        return Response.success(commentList);
    }
}

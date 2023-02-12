package com.team9.bucket_list.domain.dto.comment;

import com.team9.bucket_list.domain.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CommentListResponse {
    private Long id;        // commentId

    private String content;     // comment 내용

    private String createAt;
    private String userName;    // comment 작성자
    private Long userId;

    private Long parentId;

    @Builder.Default
    private List<CommentListResponse> children = new ArrayList<>();

    public static CommentListResponse EntityToDto(Comment comment,String userName,Long userId){
        return comment.getDeletedAt() != null ?
                CommentListResponse.builder().id(comment.getId()).content("삭제된 댓글").createAt(comment.getCreatedAt()).userName("").parentId(comment.getParent()!=null?comment.getParent().getId():null).build() :    // 삭제된 댓글일 경우
                CommentListResponse.builder().id(comment.getId()).content(comment.getContent()).createAt(comment.getCreatedAt()).userName(userName).userId(userId).parentId(comment.getParent()!=null?comment.getParent().getId():null).build();
    }
}

package com.team9.bucket_list.fixture.dto;

import com.team9.bucket_list.domain.dto.comment.CommentListResponse;
import com.team9.bucket_list.domain.entity.Comment;

public class CommentListResponseFixture {
    public static CommentListResponse get(Comment comment){
        return CommentListResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userName(comment.getMember().getUserName())
                .userId(comment.getMember().getId())
                .parentId(comment.getParent().getId())
                .build();
    }
}

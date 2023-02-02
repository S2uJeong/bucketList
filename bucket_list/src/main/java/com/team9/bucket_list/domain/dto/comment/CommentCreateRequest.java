package com.team9.bucket_list.domain.dto.comment;

import com.team9.bucket_list.domain.entity.Comment;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequest {

    private String content;         //  댓글 내용            comment

    private Long parentId;

    public Comment toEntity(Post post, Comment parent, Member member) {         // CommentRequest -> CommentEntity
        Comment comment = Comment.builder()
                .post(post)
                .parent(parent)
                .content(this.content)
                .member(member)
                .build();
        return comment;
    }

}

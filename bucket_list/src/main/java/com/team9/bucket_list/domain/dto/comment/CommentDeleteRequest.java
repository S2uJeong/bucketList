package com.team9.bucket_list.domain.dto.comment;

import com.team9.bucket_list.domain.entity.Comment;
import com.team9.bucket_list.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeleteRequest {
    private String content;         // 댓글 내용            comment

    private Long parentId;


    public Comment toEntity(Post post, Comment parent, Long writerId) {         // CommentRequest -> CommentEntity
        Comment comment = Comment.builder()
                .post(post)
                .parent(parent)
                .content(this.content)
                .writerId(writerId)
                .build();
        return comment;
    }
}

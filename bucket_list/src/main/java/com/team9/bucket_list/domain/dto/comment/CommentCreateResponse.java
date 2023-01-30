package com.team9.bucket_list.domain.dto.comment;

import com.team9.bucket_list.domain.entity.Comment;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(of = {"id","comment","userName","postId","createdAt"})
public class CommentCreateResponse {
    private Long id;            // 댓글 id
    private String comment;     // 댓글 내용

    private String userName;    // 댓글 작성자

    private Long postId;        // 포스트 id
    private Long parentId;

    private String createdAt;   // 댓글 작성 날짜

    public CommentCreateResponse(Comment comment, String writerName){
        this.id = comment.getId();
        this.comment = comment.getContent();
        this.userName = writerName;
        this.postId = comment.getPost().getId();
        this.parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        this.createdAt = comment.getCreatedAt();
    }

}

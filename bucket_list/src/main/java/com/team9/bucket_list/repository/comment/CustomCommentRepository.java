package com.team9.bucket_list.repository.comment;

import com.team9.bucket_list.domain.entity.Comment;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// querydsl에서 내가 원하는 방식대로 쿼리문을 작성하기 위해서 interface클래스 사용
// CommentRepository에 바로 사용해도 되지 않을까라고 생각도 하겠지만 querydsl에서 implements할때 commentEntity까지 전부 상속받아야 해서 커스텀으로 저장소 만듦
public interface CustomCommentRepository {
    List<Comment> findCommentByPostId(Long postId);

    Optional<Comment> findCommentByIdWithParent(@Param("id") Long id);
}

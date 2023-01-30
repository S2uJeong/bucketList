package com.team9.bucket_list.repository.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team9.bucket_list.domain.entity.Comment;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.team9.bucket_list.domain.entity.QComment.*;


// querydsl의 기본 Q-Type을 활용해야함
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {
    // querydsl 부분
    private final JPAQueryFactory queryFactory;


    // 계층구조로 편하게 바꾸기 위해 부모 댓글 내림차순, 작성일자 내림차순으로 정렬함(그래야 리스트를 받아올때 댓글, 대댓글의 순서가 잘 출력됨)
    @Override
    public List<Comment> findCommentByPostId(Long postId) {
        return queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdAt.asc()
                ).fetch();
    }

    // @Query("select c from Comment c left join fetch c.parent where c.id = :id")
    @Override
    public Optional<Comment> findCommentByIdWithParent(Long id) {
         Comment comment1 = queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.id.eq(id))
                .fetchOne();

         return Optional.ofNullable(comment1);
    }


}

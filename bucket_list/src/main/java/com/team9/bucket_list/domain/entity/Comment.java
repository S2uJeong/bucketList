package com.team9.bucket_list.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP where comment_id = ?")
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;                // 댓글 id        cno

    @Column(nullable = false)       // notnull 불가
    @Lob                            // 큰 데이터를 저장하는데 사용하는 데이터형
    private String content;         // 댓글 내용            comment


    private LocalDateTime deletedAt;     // 삭제 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;              // 해당 postId        bno

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;          // 작성자 아이디      commenter

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;         // 부모 댓글 id         pcno

    @Builder.Default                // @Builder를 사용할때 값을 초기화 하지만 @Builder.Default를 미리 선언해주면 @Builder할때 값을 초기화 안해줘도 default값 정의
    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();


    public void update(String comment){       // jpa 영속성 활용
        this.content = comment;
    }

}

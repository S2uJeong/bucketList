package com.team9.bucket_list.domain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;

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
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP where comment_id = ?")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;                // 댓글 id        cno

    @Column(nullable = false)       // notnull 불가
    @Lob                            // 큰 데이터를 저장하는데 사용하는 데이터형
    private String content;         // 댓글 내용            comment

    @Column(name = "created_date")      // 해당 열은 업데이트 불가(다른 데이터가 수정되어 해당 데이터는 고정)
    @CreatedDate            // 데이터 생성 날짜를 자동으로 주입
    private String createdAt;
    private LocalDateTime deletedAt;     // 삭제 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;              // 해당 postId        bno

    private Long writerId;          // 작성자 아이디      commenter

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;         // 부모 댓글 id         pcno

    @Builder.Default                // @Builder를 사용할때 값을 초기화 하지만 @Builder.Default를 미리 선언해주면 @Builder할때 값을 초기화 안해줘도 default값 정의
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();


    @PrePersist     // 해당 엔티티를 저장하기 이전에 실행
    public void onPrePersist(){
        // 나라를 설정하지 않고 시간을 설정하면 초단위에서 오류가 발생하여 측정이 되지 않음 따라서, ZonedDateTime를 사용하여 나라를 지정함
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    public void update(String comment){       // jpa 영속성 활용
        this.content = comment;
    }

}

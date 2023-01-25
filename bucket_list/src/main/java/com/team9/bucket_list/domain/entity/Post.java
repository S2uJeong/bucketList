package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE post SET deleted_at = current_timestamp WHERE id = ?")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private LocalDateTime deletedAt;
    private String title;
    private String content;
    @Column(nullable = false)               // not null
    private String location;
    private int cost;
    private String untilRecruit;
    private int entrantNum;
    private String eventStart;
    private String eventEnd;
    private String category;

    @Enumerated(value = EnumType.STRING)
    private PostStatus status;

//    @Enumerated(value = EnumType.STRING)
//    private PostCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

     //=== 속성 값 setting하는 메서드 ======
//    public void setCategory(PostCategory category) {
//        this.category = category;
//    }
    public void setStatus(PostStatus status) {
        this.status = status;
    }
    // ====== 지환님 필요 부분 ========
    public void modifiedContent(String content) {
        this.content = content;
    }


}

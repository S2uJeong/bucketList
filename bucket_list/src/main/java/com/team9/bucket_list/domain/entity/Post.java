package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto.member.MemberDto;
import com.team9.bucket_list.domain.dto.post.PostUpdateRequest;
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

    //==== post 수정시 사용 되는 메서드 ===//
    public void change(Post post, PostUpdateRequest postUpdateRequest) {
        post.title = postUpdateRequest.getTitle();
        post.content = postUpdateRequest.getContent();
        post.cost = postUpdateRequest.getCost();
        post.location = postUpdateRequest.getLocation();
        post.untilRecruit = postUpdateRequest.getUntilRecruit();
        post.entrantNum = postUpdateRequest.getEntrantNum();
        post.eventStart = postUpdateRequest.getEventStart();
        post.eventEnd = postUpdateRequest.getEventEnd();
        post.status = postUpdateRequest.getStatus();
        post.category = postUpdateRequest.getCategory();

    }


    // ====== 지환님 필요 부분 ========
    public void modifiedContent(String content) {
        this.content = content;
    }


}

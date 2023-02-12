package com.team9.bucket_list.domain.entity;

import com.team9.bucket_list.domain.dto.member.MemberDto;
import com.team9.bucket_list.domain.dto.post.PostUpdateRequest;
import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE post SET deleted_at = current_timestamp WHERE post_id = ?")
public class Post extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
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

    @OneToOne(mappedBy = "post")
    private ChatRoom chatRoom;

    @OneToMany(mappedBy = "post")
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

//    @OneToMany(mappedBy = "post")
//    private List<BucketlistReview> bucketlistReviewList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostFile> postFileList = new ArrayList<>();

    public void update(PostUpdateRequest request,Member member,PostStatus status) {
        this.title = request.getTitle();
        this.content=request.getContent();
        this.cost=request.getCost();
        this.location=request.getLocation();
        this.untilRecruit=request.getUntilRecruit();
        this.entrantNum=request.getEntrantNum();
        this.eventStart=request.getEventStart();
        this.eventEnd=request.getEventEnd();
        this.member= member;
        this.status=status;
        this.category=request.getCategory();

    }

    // == 편의 메서드 == //

    /**
     * 확정된 인원 반환
     */
    public Long getPermitNum() {
        return getApplicationList().stream()
                .filter(a -> a.getStatus() == 1)
                .peek(System.out::println)
                .count() + 1;
    }

    public void setRecruitComplete() {
        this.status = PostStatus.JOINCOMPLETE;
    }

    // ====== 지환님 필요 부분 ========
    public void modifiedContent (String content){
        this.content = content;
    }

    public void addComment(Comment comment) {        // CascadeType.ALL를 통해 CommentRepository에서 save를 안해주고 Post.commentList에 데이터를 넘겨줘 PostRepository에서 save해도 Comment Table에 자동 저장됨
        this.commentList.add(comment);

    }

}

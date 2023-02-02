package com.team9.bucket_list.domain.dto.post;

import com.team9.bucket_list.domain.entity.*;
import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class PostReadResponse {

    @Value("${google.map.key}")
    private Object API_KEY;// 실제 서버에서 구동할때는 무조건 환경변수에 숨겨야함 절대 노출되면 안됨!!!

    private Long postId;
    private String title; //제목
    private String content; //내용
    private int cost; //비용
    private String location; //장소
    private Double lat;
    private Double lng; // 위경도
    private String untilRecruit; //모집종료날짜
    private int entrantNum; //모집인원제한
    private String eventStart; //버킷 시작일
    private String eventEnd; //버킷 종료일
    private PostStatus status; //defalt = 모집중
    private String category; //카테고리
//    private Member member; // 버킷리스트를 만든 member --> member_id, nickname
//    private List<Application> applicationList; // 버킷리스트 참가자 목록
//    private List<Likes> likeList; // 버킷리스트 좋아요 누른 사람 목록
//    private List<Comment> commentList; // 버킷리스트의 댓글들

    // 글 하나 상세볼 때 사용하는 메서드
    public static PostReadResponse detailOf(Post post, Double lat, Double lng) {  // 위경도는 DB에 저장하지 않으므로 매개변수로 받아서 DTO화 시킨다.
        return PostReadResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .cost(post.getCost())
                .location(post.getLocation())
                .lat(lat)
                .lng(lng)
                .untilRecruit(post.getUntilRecruit())
                .entrantNum(post.getEntrantNum())
                .eventStart(post.getEventStart())
                .eventEnd(post.getEventEnd())
                .status(post.getStatus())
                .category(post.getCategory())
//                .member(post.getMember())
//                .applicationList(post.getApplicationList())
//                .likeList(post.getLikesList())
//                .commentList(post.getCommentList())
                .build();
    }

    // list로 볼 때 사용하는 메서드
    // builder로 구성된 것은 조회 시, 제목처럼 한 줄에 나와있을 내용을 나열한 것이다.
    public static Page<PostReadResponse> listOf(Page<Post> posts) {
        return posts.map(post -> PostReadResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
//                .member(post.getMember())
                .status(post.getStatus())
                .untilRecruit(post.getUntilRecruit())
                .eventStart(post.getEventStart())
                .eventEnd(post.getEventEnd())
//                .applicationList(post.getApplicationList()) // 총 승인 인원 확인
                .build()
        );
    }
}

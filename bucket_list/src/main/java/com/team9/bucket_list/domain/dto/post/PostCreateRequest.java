package com.team9.bucket_list.domain.dto.post;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostCreateRequest {

    private String title; //제목
    private String content; //내용
    private int cost; //비용
    private String location; //장소
    private String untilRecruit; //모집종료날짜
    private String entrantNum; //모집인원제한
    private String eventStart; //버킷 시작일
    private String eventEnd; //버킷 종료일
    private PostStatus status; //defalt = 모집중
    private PostCategory category; //카테고리


    public Post toEntity(Member member) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .cost(this.cost)
                .location(this.location)
                .untilRecruit(this.untilRecruit)
                .entrantNum(this.entrantNum)
                .eventEnd(this.eventEnd)
                .status(this.status)
                .category(this.category)
                .member(member)
                .build();
    }


}
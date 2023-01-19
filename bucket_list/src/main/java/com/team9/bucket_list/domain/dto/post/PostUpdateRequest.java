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
@Getter
@Builder
public class PostUpdateRequest {

    private String title; //제목
    private String content; //내용
    private int cost; //비용
    private String location; //장소
    private String untilRecruit; //모집종료날짜
    private String entrantNum; //모집인원제한
    private String eventStart; //버킷 시작일
    private String eventEnd; //버킷 종료일
    private PostStatus status; // 모집중 or 모집완료
    private PostCategory category; //카테고리

    // ========= toEntity ===============
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .cost(cost)
                .location(location)
                .untilRecruit(untilRecruit)
                .entrantNum(entrantNum)
                .eventEnd(eventEnd)
                .status(status)
                .category(category)
                .build();
    }
}

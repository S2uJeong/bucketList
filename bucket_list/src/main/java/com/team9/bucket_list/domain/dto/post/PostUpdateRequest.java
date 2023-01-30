package com.team9.bucket_list.domain.dto.post;

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

    private PostStatus status; // 모집중 or 모집완료
    private String title; //제목 ㅇ
    private String untilRecruit; //모집종료날짜 ㅇ
    private String eventStart; //버킷 시작일 ㅇ
    private String eventEnd; //버킷 종료일 ㅇ
    private int cost; //비용
    private int entrantNum; //모집인원제한 ㅇ
    private String category; //카테고리  ㅇ    // 이부분은 enum으로 안해도 됨, 프론트에서 드랍다운 메뉴로 선택하게끔하고 해당 값을 보내줄수 있음

    private String location; //장소 ㅇ

    private String content; //내용 ㅇ



    // ========= toEntity ===============
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .cost(cost)
                .location(location)
                .untilRecruit(untilRecruit)
                .entrantNum(entrantNum)
                .eventStart(eventStart)
                .eventEnd(eventEnd)
                .status(status)
                .category(category)
                .build();
    }
}
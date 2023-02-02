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
@Builder
@Getter
public class PostCreateRequest {

    private String title; //제목 ㅇ
    private String untilRecruit; //모집종료날짜 ㅇ
    private String eventStart; //버킷 시작일 ㅇ
    private String eventEnd; //버킷 종료일 ㅇ
    private String category; //카테고리  ㅇ    // 이부분은 enum으로 안해도 됨, 프론트에서 드랍다운 메뉴로 선택하게끔하고 해당 값을 보내줄수 있음
    private int entrantNum; //모집인원제한 ㅇ
    private int cost; //비용

    private String location; //장소 ㅇ

    private String content; //내용 ㅇ



    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .cost(this.cost)
                .location(this.location)
                .untilRecruit(this.untilRecruit)
                .entrantNum(this.entrantNum)
                .eventStart(this.eventStart)
                .eventEnd(this.eventEnd)
                .status(PostStatus.JOIN)             // enum에서 값 대입
                .category(this.category)
                .build();
    }
}
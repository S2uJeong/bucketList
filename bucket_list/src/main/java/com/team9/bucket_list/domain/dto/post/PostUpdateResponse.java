package com.team9.bucket_list.domain.dto.post;
import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PostUpdateResponse {
    private Long id;
    private String title; //제목
    private String content; //내용
    private int cost; //비용
    private String location; //장소
    private String untilRecruit; //모집종료날짜
    private int entrantNum; //모집인원제한
    private String eventStart; //버킷 시작일
    private String eventEnd; //버킷 종료일
    private String status; // 모집중 or 모집완료
    private String category; //카테고리

    // 수정 시에, DB에 있는 이전 게시물 내용 띄우기 위한 메서드
    public static PostUpdateResponse prePost(Post post) {
        // DB에 enum 형식으로 저장 된 것을 프론트에 string으로 보이게 하기 위해 switch문을 넣었다.
        String statusKO = "";

        switch (post.getStatus()) {
            case JOIN -> statusKO = "모집중";
            case JOINCOMPLETE -> statusKO = "모집완료";
            default -> statusKO = "알수없음";
        }
        // jpa가 변경감지 할 수 있도록 post.id를 넣어주었다.
        // status는 switch문으로 변환한 String으로 대신 넣었다.
        return PostUpdateResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .cost(post.getCost())
                .location(post.getLocation())
                .untilRecruit(post.getUntilRecruit())
                .entrantNum(post.getEntrantNum())
                .eventStart(post.getEventStart())
                .eventEnd(post.getEventEnd())
                .status(statusKO)
                .category(post.getCategory())
                .build();

    }

}

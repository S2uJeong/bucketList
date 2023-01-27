package com.team9.bucket_list.domain.dto.post;

import com.team9.bucket_list.domain.entity.Post;
import com.team9.bucket_list.domain.enumerate.PostCategory;
import com.team9.bucket_list.domain.enumerate.PostStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter @Setter
@Builder
public class PostUpdateRequest {

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


}
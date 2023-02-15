package com.team9.bucket_list.fixture.dto;

import com.team9.bucket_list.domain.dto.post.PostReadResponse;
import com.team9.bucket_list.domain.entity.Post;

public class PostReadResponseFixture {

    public static PostReadResponse get(Post post,Double lat,Double lng){
        return PostReadResponse.builder()
                .postId(post.getId())
                .userName(post.getMember().getUserName())
                .userId(post.getMember().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .cost(post.getCost())
                .category(post.getCategory())
                .location(post.getLocation())
                .lng(lat)
                .lat(lng)
                .untilRecruit(post.getUntilRecruit())
                .entrantNum(post.getEntrantNum()) //모집인원제한
                .eventStart(post.getEventStart()) //버킷 시작일
                .eventEnd(post.getEventEnd()) //버킷 종료일
                .status(post.getStatus()) //defalt = 모집중
                .fileName("noImage") //S3에 저장된 이미지 파일이름
                .permitNum(1l)
                .fileId(0l) // DB에 저장된 파일 ID
                .build();
    }
}

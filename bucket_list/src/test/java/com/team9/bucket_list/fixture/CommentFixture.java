package com.team9.bucket_list.fixture;


import com.team9.bucket_list.domain.entity.Comment;
import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.entity.Post;
import jakarta.persistence.*;

import java.time.LocalDateTime;

// 임의 Entity Response 값
public class CommentFixture {
    public static Comment get(Long commentid, Post post, Comment parent, Member member, String content) {
        return Comment.builder()
                .id(commentid)
                .post(post)
                .parent(parent)
                .content(content)
                .member(member)
                .build();
    }

}

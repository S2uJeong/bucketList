package com.team9.bucket_list.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Alarm extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    //0 : 안읽음, 1 : 읽음
    private byte readStatus;

    //0 : 댓글, 1 : 좋아요, 2 : 참가자가 신청서 작성, 3 : 신청서 승낙, 4 : 기타
    private byte category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long postId;
    private String postTitle;
    private String senderName;

    public static Alarm save(byte category, Member member, Long postId, String postName, String senderName) {
        return Alarm.builder()
                .readStatus((byte) 0)
                .category(category)
                .member(member)
                .postId(postId)
                .postTitle(postName)
                .senderName(senderName)
                .build();
    }

    public static Alarm updateRead(Alarm alarm, byte readStatus) {
        return Alarm.builder()
                .id(alarm.getId())
                .readStatus(readStatus)
                .senderName(alarm.getSenderName())
                .member(alarm.getMember())
                .category(alarm.getCategory())
                .postId(alarm.getPostId())
                .postTitle(alarm.getPostTitle())
                .build();
    }
}

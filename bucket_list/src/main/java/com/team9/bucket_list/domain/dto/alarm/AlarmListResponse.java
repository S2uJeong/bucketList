package com.team9.bucket_list.domain.dto.alarm;

import com.team9.bucket_list.domain.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
@AllArgsConstructor
public class AlarmListResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private byte category;
    private String senderName;

    public static Page<AlarmListResponse> toList(Page<Alarm> alarms) {
        return alarms.map(alarm -> AlarmListResponse.builder()
                .id(alarm.getId())
                .comment(alarm.getContent())
                .userName(alarm.getMember().getUserName())
                .postId(alarm.getPostId())
                .senderName(alarm.getSenderName())
                .build());
    }
}

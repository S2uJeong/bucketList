package com.team9.bucket_list.domain.dto;

import com.team9.bucket_list.domain.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmResponse {
    private Long memberId;
    private String content;

    public static Page<AlarmResponse> response(Page<Alarm> alarms) {
        Page<AlarmResponse> result = alarms.map(alarm -> AlarmResponse.builder()
                .memberId(alarm.getMember().getId())
                .content(alarm.getContent())
                .build());
        return result;
    }
}

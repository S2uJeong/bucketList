package com.team9.bucket_list.domain.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatOutRequest {
    Long memberId;
    Long roomId;
}

package com.team9.bucket_list.domain.dto.chat;

import com.team9.bucket_list.domain.Response;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
@Getter
public class ChatMessageResponse<T> {
    private String resultCode;
    private String userName;
    private T result;

    public static <T> ChatMessageResponse<T> success(T result, String userName) {
        return ChatMessageResponse.<T>builder()
                .resultCode("SUCCESS")
                .userName(userName)
                .result(result)
                .build();
    }

    public static <T> ChatMessageResponse<T> error(T result, String userName) {
        return ChatMessageResponse.<T>builder()
                .resultCode("ERROR")
                .userName(userName)
                .result(result)
                .build();
    }
}

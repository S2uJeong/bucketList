package com.team9.bucket_list.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> success(T result) {
        return Response.<T>builder()
                .resultCode("SUCCESS")
                .result(result)
                .build();
    }

    public static <T> Response<T> error(T result) {
        return Response.<T>builder()
                .resultCode("ERROR")
                .result(result)
                .build();
    }
}

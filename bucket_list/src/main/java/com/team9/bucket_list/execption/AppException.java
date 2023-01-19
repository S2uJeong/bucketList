package com.team9.bucket_list.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException{

    // 왜 이렇게 import 되는거지?
    private com.finalproject_sujeongchoi_team9.exception.ErrorCode errorCode;
    private String message;

    @Override
    public String getMessage() {
        if(message == null) return errorCode.getMessage();
        return String.format("%s. %s", errorCode.getMessage(), message);
    }

}


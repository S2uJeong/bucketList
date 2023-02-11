package com.team9.bucket_list.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Email이 중복됩니다."),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "userName 이 중복됩니다."),
    INCORRECT_PASSWORD_CORRECT(HttpStatus.CONFLICT, "Email이 중복됩니다."),
    INVALID_PASSWORD(HttpStatus.NOT_FOUND, "패스워드가 잘못되었습니다."),
    USERNAME_NOT_FOUNDED(HttpStatus.NOT_FOUND, "해당 사용자는 없습니다."),
    REFRESH_TOKEN_NOT_FOUNDED(HttpStatus.NOT_FOUND, "해당 사용자에 대한 리프레시 토큰이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "refresh token 만료"),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 없습니다."),
    DUPLICATED_LIKE(HttpStatus.CONFLICT, "같은 글에 좋아요를 두 번 눌렀습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리뷰가 없습니다."),
    BUCKETLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 버킷리스트가 없습니다."),
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 신청서가 없습니다"),
    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알람이 없습니다."),
    // POST 관련
    FILE_NOT_EXISTS(HttpStatus.NOT_FOUND, "빈 파일 입니다."),
    EXCEED_ENTRANT_NUM(HttpStatus.CONFLICT, "참가자 수를 초과하였습니다."),

    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    WRONG_FILE_FORMAT(HttpStatus.BAD_REQUEST, "파일 형식이 틀립니다."),

    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 프로필이 없습니다."),

    CHAT_ROOM_NOT_FOUNT(HttpStatus.NOT_FOUND,"채팅방을 찾을 수 없습니다");


    private HttpStatus status;
    private String message;

}
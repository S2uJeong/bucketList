package com.team9.bucket_list.domain.dto.member;

import lombok.*;

@Data
public class MemberLoginRequest {
    private String email;
    private String password;
}

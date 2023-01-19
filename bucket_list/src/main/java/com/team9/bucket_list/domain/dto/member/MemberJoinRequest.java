package com.team9.bucket_list.domain.dto.member;

import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.enumerate.Gender;
import com.team9.bucket_list.domain.enumerate.MemberRole;

import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinRequest {
    private String userName;
    private String email;
    private String password;
    private String passwordCorrect;
    private String gender;
    private int age;

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .userName(this.userName)
                .age(this.age)
                .gender(Gender.valueOf(this.gender))
                .memberRole(MemberRole.USER)
                .postRemain(3)
                .build();
    }
}

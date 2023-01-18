package com.team9.bucket_list.domain.dto.Member;

import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.enumerate.Gender;
import com.team9.bucket_list.domain.enumerate.UserRole;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinRequest {
    private String username;
    private String email;
    private String password;
    private String passwordCorrect;
    private String gender;
    private int age;

    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .age(this.age)
                .gender(Gender.valueOf(this.gender))
                .userRole(UserRole.USER)
                .postRemain(3)
                .build();
    }
}

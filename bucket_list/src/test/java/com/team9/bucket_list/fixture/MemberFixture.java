package com.team9.bucket_list.fixture;


import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.enumerate.Gender;
import com.team9.bucket_list.domain.enumerate.MemberRole;

// 임의 Entity Response 값
public class MemberFixture {
    public static Member get(String email, String password, String userName) {
        Member entity = Member.builder()
                .id(1L)
                .email(email)
                .password(password)
                .userName(userName)
                .age(20)
                .oauthId("test")
                .gender(Gender.MAN)
                .memberRole(MemberRole.USER)
                .build();

        return entity;
    }

}

package com.team9.bucket_list.domain.dto.member;

import com.team9.bucket_list.domain.entity.Member;
import com.team9.bucket_list.domain.enumerate.Gender;
import com.team9.bucket_list.domain.enumerate.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@Getter
@Builder
public class MemberProfile implements OAuth2User {      //Resource Server마다 제공하는 정보가 다르므로 통일시키기 위한 profile
    private String userName; //authentication의 name
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes; // oauthId, name, email
    public Member googleToMember(){
        return Member.builder()
                .oauthId((String) this.attributes.get("oauthId"))
                .userName(((String) this.attributes.get("email")).split("@")[0])
                .email((String) this.attributes.get("email"))
                .memberRole(MemberRole.USER)
                .build();
    }

    public Member naverToMember(){
        return Member.builder()
                .oauthId((String) this.attributes.get("oauthId"))
                .userName(((String) this.attributes.get("email")).split("@")[0])
                .email((String) this.attributes.get("email"))
                .memberRole(MemberRole.USER)
                .gender(getGender((String) this.attributes.get("gender")))
                .age(getAge((String) this.attributes.get("birthYear")))
                .build();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.userName;
    }

    public static Gender getGender(String gender) {
        switch(gender) {
            case "F":
                return Gender.WOMAN;
            case "M":
                return Gender.MAN;
            default:
                return Gender.ETC;
        }
    }

    public static Integer getAge(String birthYear) {
        return LocalDate.now().getYear() - Integer.parseInt(birthYear) + 1;
    }
}

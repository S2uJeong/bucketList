package com.team9.bucket_list.security.config;


import com.team9.bucket_list.repository.RefreshTokenRepository;
import com.team9.bucket_list.security.entrypoint.CustomAuthenticationEntryPoint;
import com.team9.bucket_list.security.filter.JwtFilter;
import com.team9.bucket_list.security.utils.JwtUtil;
import com.team9.bucket_list.service.MemberService;
import com.team9.bucket_list.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final OAuthService oAuthService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and()

                .authorizeHttpRequests(authorize -> authorize
                        .shouldFilterAllDispatcherTypes(false)
                        .requestMatchers("/**")
                        .permitAll()
                        .requestMatchers("/","/post/**","/comment/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
//                .anyRequest().hasRole("ADMIN")
//                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
//                .and()
//                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
//                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/login").and()
                .userInfoEndpoint().userService(oAuthService).and()
//                .successHandler(OAuth2AuthenticationSuccessHandler)      //OAuth2 로그인 성공 시 호출한 handler
//                .failureHandler(authenticationFailureHandelr).and()
                .and()
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) //UserNamePasswordAuthenticationFilter적용하기 전에 JWTTokenFilter를 적용 하라는 뜻 입니다.
                .build();
    }
}

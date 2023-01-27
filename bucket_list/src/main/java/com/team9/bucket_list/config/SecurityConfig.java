package com.team9.bucket_list.config;

import com.team9.bucket_list.security.JwtFilter;
import com.team9.bucket_list.security.OAuth2AuthenticationSuccessHandler;
import com.team9.bucket_list.service.OAuthService;
import com.team9.bucket_list.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .shouldFilterAllDispatcherTypes(false)
                        .requestMatchers("/**","/post/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
//                .anyRequest().hasRole("ADMIN")
//                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
//                .and()
//                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
//                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/login")
//                .and()
//                .redirectionEndpoint().baseUri("/login/callback/*")
                .and()
                .userInfoEndpoint().userService(oAuthService)    //provider로부터 획득한 유저정보를 다룰 service단을 지정한다.
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)      //OAuth2 로그인 성공 시 호출한 handler
//                .failureHandler(authenticationFailureHandelr)
                .and()
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtExceptionFilter, JwtFilter.class)
                .build();
    }
}

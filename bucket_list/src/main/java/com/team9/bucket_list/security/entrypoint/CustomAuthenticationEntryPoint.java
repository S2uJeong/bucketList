package com.team9.bucket_list.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team9.bucket_list.domain.ErrorResponse;
import com.team9.bucket_list.execption.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * header에 Authorization을 포함하지 않으면
 * REQUEST_DENIED 에러를 발생
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        final ErrorCode ERROR_CODE = ErrorCode.INVALID_PERMISSION;
        ObjectMapper objectMapper = new ObjectMapper();

        ErrorResponse errorResponse = new ErrorResponse(ERROR_CODE.toString(), ERROR_CODE.getMessage());

        response.setStatus(403);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}


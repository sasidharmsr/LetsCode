package com.example.Let.sCode.Security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Handle unauthorized requests here, for example:
        log.warn("Responding with unauthorized error. Message - {}", authException.getMessage());
        if(response.getHeader("token")!=null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            String responseBody = "{\"error\": \"Unauthorized\",\"message\": \"Token Expired\",\"code\": 400}";
            PrintWriter writer = response.getWriter();
            writer.print(responseBody);writer.flush();
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
         // Create a JSON response body

        String responseBody = "{\"error\": \"Unauthorized\",\"message\": \"Access denied\",\"code\": 401}";

        // Write the JSON response to the output stream
        PrintWriter writer = response.getWriter();
        writer.print(responseBody);writer.flush();

    }
}

package com.example.Let.sCode.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import org.springframework.security.core.userdetails.User;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static String userName="";
    public static Integer userId=0;
    public static String userRole="";

    

    @Autowired
    private JwtTokenService jwtTokenService;


    @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    System.out.println(request.getServletPath().contains("/api/auth"));
    response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Content-Length, X-Requested-With");
        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
          }
          final String authHeader = request.getHeader("Authorization");
          final String jwt;
          if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
          }
          jwt = authHeader.substring(7);
           try {
                if (!jwtTokenService.isTokenExpired(jwt)) {
                    userName = jwtTokenService.extractUsername(jwt);
                    List<String> claims=jwtTokenService.getClaims(jwt);
                    userId=Integer.parseInt(claims.get(1));
                    userRole=claims.get(0);
                    List<String> roles=new ArrayList<>();roles.add(userRole);
                    System.out.println(userId+"--"+userRole);
                    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                      UserDetails userDetails =  new User(userName, "", roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                      System.out.println(userName);
                      if (jwtTokenService.isTokenValid(jwt, userDetails) ) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                      }
                    }
                }
                else{
                    response.addHeader("token", "Expired");
                }
            } catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException e) {
                if (e instanceof ExpiredJwtException)
                    response.addHeader("token", "Expired");
                logger.error("Unable to get JWT Token or JWT Token has expired");
          }
          filterChain.doFilter(request, response);
    }
    

    // private void setSecurityContext(WebAuthenticationDetails authDetails, String token) {
    //     final String username = jwtTokenService.extractUsername(token);
    //     final List<String> roles = jwtTokenService.getRoles(token);
    //     final UserDetails userDetails = new User(username, "", roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    //     final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
    //             userDetails.getAuthorities());
    //     authentication.setDetails(authDetails);
    //     // After setting the Authentication in the context, we specify
    //     // that the current user is authenticated. So it passes the
    //     // Spring Security Configurations successfully.
    //     SecurityContextHolder.getContext().setAuthentication(authentication);
    // }

}
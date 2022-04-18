package com.example.springsecuritybasic.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  // login 요청을 하면 로그인 시도를 위해서 실행되는 method
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    System.out.println("JwtAuthenticationFilter : 로그인 시도중");

    // 1. username, password 받아서
    // 2. 정상적으로 로그인 되면, authenticationManager로 로그인 시도를 하면
    // PrincipalDetailsService의 loadUserByUserName이 실행됨

    // 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해서 필요?)
    // 4. JWT 토큰을 만들어서 응답해주면 됨

    return super.attemptAuthentication(request, response);
  }
}

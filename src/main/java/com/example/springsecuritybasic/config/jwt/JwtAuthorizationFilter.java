package com.example.springsecuritybasic.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springsecuritybasic.config.auth.PrincipalDetails;
import com.example.springsecuritybasic.model.User;
import com.example.springsecuritybasic.repository.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// Security Filter 중에, BasicAuthenticationFilter가 있음
// 권한이나, 인증이 필요한 경우 위 Filter를 무조건 탐

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final UserRepository userRepository;

  public JwtAuthorizationFilter(
      AuthenticationManager authenticationManager,
      UserRepository userRepository) {
    super(authenticationManager);
    this.userRepository = userRepository;
  }

  // 인증이나 권한이 필요한 주소요청이 있을때 해당 필터를 타게됨
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    System.out.println("인증이나 권한이 필요한 주소 요청이 됨");

    String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);

    if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
      chain.doFilter(request, response);
      return;
    }

    String jwtToken = request.getHeader(JwtProperties.HEADER_STRING)
        .replace(JwtProperties.TOKEN_PREFIX, "");
    String userName = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
        .build().verify(jwtToken).getClaim("username")
        .asString();

    if (userName != null) {
      User user = userRepository.findByUsername(userName);

      PrincipalDetails principalDetails = new PrincipalDetails(user);
      Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,
          null,
          principalDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);

    }
    chain.doFilter(request, response);

  }
}

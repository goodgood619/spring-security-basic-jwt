package com.example.springsecuritybasic.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springsecuritybasic.config.auth.PrincipalDetails;
import com.example.springsecuritybasic.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  // /login 요청을 하면 로그인 시도를 위해서 실행되는 method
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    System.out.println("JwtAuthenticationFilter : 로그인 시도중");

    // 1. username, password 받아서
    try {
//      BufferedReader br = request.getReader();
//      String input = null;
//      while((input = br.readLine())!=null) {
//        System.out.println(input);
//      }
      ObjectMapper objectMapper = new ObjectMapper();
      User user = objectMapper.readValue(request.getInputStream(), User.class);

      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          user.getUsername(), user.getPassword());

      // PrincipalDetailsService의 loadUserByName method 실행
      // DB에 있는 username과 password가 일치한다
      Authentication authenticate = authenticationManager.authenticate(authenticationToken);

      PrincipalDetails principal = (PrincipalDetails) authenticate.getPrincipal();
      System.out.println(principal.getUser().getUsername());
      // authentication 객체가 session영역에 저장됨 (방법은 return으로)
      // 단지 권한처리때문에 session영역에 저장을 하는것임
      return authenticate;
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 2. 정상적으로 로그인 되면, authenticationManager로 로그인 시도를 하면
    // PrincipalDetailsService의 loadUserByUserName이 실행됨

    // 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해서 필요?)
    // 4. JWT 토큰을 만들어서 응답해주면 됨

    return null;
  }

  // attemptAuthentication 실행 후 정상적으로 되면 successfulAuthentication 실행
  // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT token을 response 해주면됨
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {

    System.out.println("인증이 완료 되었다는 의미");
    PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();

    // RSA방식이 아닌, Hash암호방식
    String jwtToken = JWT.create()
        .withSubject(principal.getUsername())
        .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
        .withClaim("id", principal.getUser().getId())
        .withClaim("username", principal.getUser().getUsername())
        .sign(Algorithm.HMAC512(JwtProperties.SECRET));

    response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
  }
}

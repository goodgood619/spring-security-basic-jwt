package com.example.springsecuritybasic.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter1 implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse res = (HttpServletResponse) servletResponse;

    // id, pw 로그인 완료되면 그걸 응답해준다
    // 요청할때마다 header에 Authorization에 value가 들어옴
    // 그때 토큰이 서버가 만든건지 아닌건지만 검증하면됨 (RSA, HS256)
    if (req.getMethod().equals("POST")) {
      String headerAuth = req.getHeader("Authorization");
      System.out.println(headerAuth);

      if (headerAuth.equals("cos")) {
        filterChain.doFilter(servletRequest, servletResponse);
      } else {
        PrintWriter outPrintWriter = res.getWriter();
        outPrintWriter.println("인증안됨");
      }
    }
  }
}

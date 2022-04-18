package com.example.springsecuritybasic.config;

import com.example.springsecuritybasic.filter.MyFilter1;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean // Security Filter Chain이 아닌, 별도의 FilterChain
  // 동작순서
  // Security Filter Chain의 addBeforeChain
  // afterBeforeChain 순서로 실행됨
  // 그다음은 별도의 FilterChain (가장 마지막에 실행됨)
  public FilterRegistrationBean<MyFilter1> filter1() {
    FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
    bean.addUrlPatterns("/*");
    bean.setOrder(0); // 필터 번호가 낮은게 먼저 실행됨
    return bean;
  }

}

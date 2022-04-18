package com.example.springsecuritybasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfigure {

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration configuration = new CorsConfiguration();
    configuration
        .setAllowCredentials(true); // 내서버가 응답할 때, response json을 자바스크립트에서 처리 할수 있게 하기 할지를 결정
    configuration.addAllowedOrigin("*"); // 모든 ip 응답 허용
    configuration.addAllowedHeader("*"); // 모든 header에 응답 허용
    configuration.addAllowedMethod("*"); // 모든 get,post,put,delete 요청 허용
    source.registerCorsConfiguration("/api/**", configuration);
    return new CorsFilter(source);
  }

}

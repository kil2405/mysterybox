package com.weplaylabs.mysterybox.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")  // 모든 경로에 대해 CORS 적용
                //.allowedOrigins("http://43.201.15.203:5000") // 허용할 도메인 지정
                .allowedOrigins("http://43.201.15.203:5000", "http://192.168.0.54:5500") // 허용할 도메인 지정
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION") // 허용할 HTTP 메서드
                .allowedHeaders("userId", "Content-Type")  // 허용할 헤더 명시
                .allowCredentials(true) // 인증 정보 허용 (쿠키 등)
                .maxAge(3600);  // 캐시 시간 설정
    }
}

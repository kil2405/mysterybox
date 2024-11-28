package com.weplaylabs.mysterybox.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CorsDebugController {
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handlePreflightRequest() {
        HttpHeaders headers = new HttpHeaders();
        
        // Access-Control-Allow-Origin: 허용할 도메인 설정 (예: 모든 도메인 허용)
        headers.add("Access-Control-Allow-Origin", "http://43.201.15.203:5000");
        
        // Access-Control-Allow-Methods: 허용할 HTTP 메서드 설정
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        // Access-Control-Allow-Headers: 허용할 헤더 설정 (Authorization 및 Custom Header 허용)
        headers.add("Access-Control-Allow-Headers", "userId, Content-Type");
        
        // Access-Control-Allow-Credentials: 인증 정보(Cookie 등) 허용
        headers.add("Access-Control-Allow-Credentials", "true");
        
        // Access-Control-Max-Age: 프리플라이트 요청 캐싱 시간 (초 단위)
        headers.add("Access-Control-Max-Age", "3600");

        // 200 OK 응답 반환 및 헤더 설정
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
    
}

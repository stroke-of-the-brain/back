package com.naebom.stroke.naebom.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String AI_SERVER_URL = "http://192.168.9.160:5000/api/ai/send"; // Flask AI 서버 URL

    public String sendImageToAi(String base64Image) {
        // 요청 데이터(JSON)
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("image", base64Image);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 요청 생성
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Flask 서버에 POST 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                AI_SERVER_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response.getBody(); // Flask의 응답을 반환
    }
}

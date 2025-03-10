package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendImageToAi(@RequestBody Map<String, String> request) {
        String base64Image = request.get("image"); // 프론트에서 보낸 Base64 이미지 받기
        if (base64Image == null || base64Image.isEmpty()) {
            return ResponseEntity.badRequest().body("No image data provided");
        }

        String aiResponse = aiService.sendImageToAi(base64Image); // Flask로 이미지 전송
        return ResponseEntity.ok(aiResponse);
    }
}

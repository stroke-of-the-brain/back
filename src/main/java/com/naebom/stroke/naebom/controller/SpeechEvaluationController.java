package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.SpeechEvaluationRequestDto;
import com.naebom.stroke.naebom.dto.SpeechEvaluationResponseDto;
import com.naebom.stroke.naebom.service.SpeechToTextService;
import com.naebom.stroke.naebom.utils.LevenshteinUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/speech")
@CrossOrigin(origins = "*")
public class SpeechEvaluationController {

    private final SpeechToTextService speechToTextService;

    public SpeechEvaluationController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<SpeechEvaluationResponseDto> evaluateSpeech(@RequestBody SpeechEvaluationRequestDto request) {
        try {
            // 음성을 텍스트로 변환
            String recognizedText = speechToTextService.transcribeSpeech(request.getBase64Audio());

            // 예제 문장과 비교하여 유사도 점수 계산
            int distance = LevenshteinUtil.levenshteinDistance(request.getExpectedText(), recognizedText);
            int maxLength = Math.max(request.getExpectedText().length(), recognizedText.length());
            double score = Math.max(0, 100 - ((double) distance / maxLength * 100));

            return ResponseEntity.ok(new SpeechEvaluationResponseDto(score));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SpeechEvaluationResponseDto(0));
        }
    }
}

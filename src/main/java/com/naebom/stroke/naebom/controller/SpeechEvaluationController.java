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

            // 유사도 점수 계산
            double score = calculateScore(request.getExpectedText(), recognizedText);

            return ResponseEntity.ok(new SpeechEvaluationResponseDto(score));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SpeechEvaluationResponseDto(0));
        }
    }

    private double calculateScore(String expectedText, String recognizedText) {
        int correctCount = 0;
        int totalLength = expectedText.length();

        int minLength = Math.min(expectedText.length(), recognizedText.length());

        // 올바르게 인식된 문자 수 계산
        for (int i = 0; i < minLength; i++) {
            if (expectedText.charAt(i) == recognizedText.charAt(i)) {
                correctCount++;
            }
        }

        // Levenshtein 거리 계산 (오타 개수 확인)
        int distance = LevenshteinUtil.levenshteinDistance(expectedText, recognizedText);

        // 총 감점 계산
        double penalty = ((double) distance / totalLength) * 100;

        // 점수 계산 (최대 100점)
        double score = Math.max(0, 100 - penalty);

        return score;
    }
}

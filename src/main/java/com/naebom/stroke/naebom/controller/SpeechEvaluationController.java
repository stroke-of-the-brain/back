package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.SpeechEvaluationRequestDto;
import com.naebom.stroke.naebom.dto.SpeechEvaluationResponseDto;
import com.naebom.stroke.naebom.service.SpeechToTextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/speech")
@CrossOrigin(origins = "*")
public class SpeechEvaluationController {

    private static final Logger logger = LoggerFactory.getLogger(SpeechEvaluationController.class);
    private final SpeechToTextService speechToTextService;

    public SpeechEvaluationController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<SpeechEvaluationResponseDto> evaluateSpeech(@RequestBody SpeechEvaluationRequestDto request) {
        try {
            double score = speechToTextService.evaluateSpeech(request.getBase64Audio(), request.getExpectedText());

            logger.info("최종 점수: {}", score);
            return ResponseEntity.ok(new SpeechEvaluationResponseDto(score));
        } catch (Exception e) {
            logger.error("음성 평가 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new SpeechEvaluationResponseDto(0));
        }
    }
}
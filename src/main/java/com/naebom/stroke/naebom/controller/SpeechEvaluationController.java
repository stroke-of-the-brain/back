package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.SpeechEvaluationRequestDto;
import com.naebom.stroke.naebom.dto.SpeechEvaluationResponseDto;
import com.naebom.stroke.naebom.service.SpeechToTextService;
import com.naebom.stroke.naebom.service.TestRecordService;
import com.naebom.stroke.naebom.utils.SpeechScoreBuffer;
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
    private final TestRecordService testRecordService;
    private final SpeechScoreBuffer speechScoreBuffer;

    public SpeechEvaluationController(
            SpeechToTextService speechToTextService,
            TestRecordService testRecordService,
            SpeechScoreBuffer speechScoreBuffer) {
        this.speechToTextService = speechToTextService;
        this.testRecordService = testRecordService;
        this.speechScoreBuffer = speechScoreBuffer;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<SpeechEvaluationResponseDto> evaluateSpeech(@RequestBody SpeechEvaluationRequestDto request) {
        try {
            Long memberId = request.getMemberId();
            double score = speechToTextService.evaluateSpeech(request.getBase64Audio(), request.getExpectedText());

            logger.info("사용자 {}의 발음 점수: {}", memberId, score);

            // 점수 누적
            speechScoreBuffer.addScore(memberId, score);

            // 3개 모이면 평균 계산 → 저장
            if (speechScoreBuffer.isReady(memberId)) {
                double avg = speechScoreBuffer.getScores(memberId)
                        .stream().mapToDouble(Double::doubleValue).average().orElse(0);
                logger.info("사용자 {} 발음 평균 점수 저장: {}", memberId, avg);

                // 저장 로직
                testRecordService.saveSpeechTestScore(memberId, avg);

                // 버퍼 초기화
                speechScoreBuffer.clear(memberId);
            }

            return ResponseEntity.ok(new SpeechEvaluationResponseDto(score));

        } catch (Exception e) {
            logger.error("음성 평가 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(new SpeechEvaluationResponseDto(0));
        }
    }
}

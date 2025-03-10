package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.TestRecordDto;
import com.naebom.stroke.naebom.service.TestRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test-records")
@RequiredArgsConstructor
public class TestRecordController {

    private final TestRecordService testRecordService;

    //검사 결과 저장 (새로운 검사 결과를 DB에 추가)
    @PostMapping("/save")
    public ResponseEntity<TestRecordDto> saveTestRecord(@RequestBody TestRecordDto dto) {
        TestRecordDto savedRecord = testRecordService.saveTestRecord(dto);
        return ResponseEntity.ok(savedRecord);
    }
    // ✅ 손가락 테스트 (fingerTestScore)만 저장
    @PostMapping("/save-finger-score")
    public ResponseEntity<String> saveFingerTestScore(@RequestBody Map<String, Object> request) {
        Long memberId = ((Number) request.get("memberId")).longValue();
        Double fingerTestScore = ((Number) request.get("fingerTestScore")).doubleValue();

        testRecordService.saveFingerTestScore(memberId, fingerTestScore);
        return ResponseEntity.ok("Finger test score saved successfully.");
    }

    /*// ✅ AI에서 얼굴 검사 결과 (faceTestScore)를 받아 저장
    @PostMapping("/save-face-score")
    public ResponseEntity<String> saveFaceTestScore(@RequestBody Map<String, Object> request) {
        Long memberId = ((Number) request.get("memberId")).longValue();
        Double faceTestScore = ((Number) request.get("faceTestScore")).doubleValue();

        testRecordService.saveFaceTestScore(memberId, faceTestScore);
        return ResponseEntity.ok("Face test score saved successfully.");
    }

    // ✅ 발음 테스트 점수를 3번 저장 후, 평균 점수를 계산하여 저장
    @PostMapping("/save-speech-score")
    public ResponseEntity<String> saveSpeechTestScore(@RequestBody Map<String, Object> request) {
        Long memberId = ((Number) request.get("memberId")).longValue();
        Double speechTestScore = ((Number) request.get("speechTestScore")).doubleValue();

        testRecordService.saveSpeechTestScore(memberId, speechTestScore);
        return ResponseEntity.ok("Speech test score updated successfully.");
    }*/

    //모든 검사 기록 조회
    @GetMapping("/history/{memberId}")
    public ResponseEntity<List<TestRecordDto>> getTestHistory(@PathVariable Long memberId) {
        List<TestRecordDto> history = testRecordService.getTestHistory(memberId);
        return ResponseEntity.ok(history);
    }

    //가장 최근 2개의 검사 기록 조회
    @GetMapping("/recent/{memberId}")
    public ResponseEntity<List<TestRecordDto>> getRecentTwoTestRecords(@PathVariable Long memberId) {
        List<TestRecordDto> recentRecords = testRecordService.getRecentTwoTestRecords(memberId);
        return ResponseEntity.ok(recentRecords);
    }
    // ✅ 최근 2개의 검사 기록 조회 (간단한 데이터만 반환, null 제거)
    @GetMapping("/recent-simple/{memberId}")
    public ResponseEntity<List<Map<String, Object>>> getRecentTwoSimpleTestRecords(@PathVariable Long memberId) {
        List<Map<String, Object>> recentRecords = testRecordService.getRecentTwoSimpleTestRecords(memberId);
        return ResponseEntity.ok(recentRecords);
    }

    // ✅ 모든 검사 기록 조회 (간단한 데이터만 반환, null 제거)
    @GetMapping("/history-simple/{memberId}")
    public ResponseEntity<List<Map<String, Object>>> getSimpleTestHistory(@PathVariable Long memberId) {
        List<Map<String, Object>> history = testRecordService.getSimpleTestHistory(memberId);
        return ResponseEntity.ok(history);
    }
}

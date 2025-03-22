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
    //////////
    @PostMapping("/save-face-score")
    public ResponseEntity<?> saveFace(@RequestBody Map<String, Object> body) {
        testRecordService.saveFaceTestScore(
                Long.valueOf(body.get("memberId").toString()),
                Double.valueOf(body.get("faceTestScore").toString()));
        return ResponseEntity.ok("face 저장 완료");
    }

    @PostMapping("/save-finger-score")
    public ResponseEntity<?> saveFinger(@RequestBody Map<String, Object> body) {
        testRecordService.saveFingerTestScore(
                Long.valueOf(body.get("memberId").toString()),
                Double.valueOf(body.get("fingerTestScore").toString()));
        return ResponseEntity.ok("finger 저장 완료");
    }

    @PostMapping("/save-arm-score")
    public ResponseEntity<?> saveArm(@RequestBody Map<String, Object> body) {
        testRecordService.saveArmTestScore(
                Long.valueOf(body.get("memberId").toString()),
                Double.valueOf(body.get("armMuscleScore").toString()));
        return ResponseEntity.ok("arm 저장 완료");
    }
    @PostMapping("/save-feedback-risk")
    public ResponseEntity<?> saveFeedbackAndRisk(@RequestBody Map<String, Object> body) {
        Long memberId = Long.valueOf(body.get("memberId").toString());
        //Boolean strokeRisk = Boolean.valueOf(body.get("strokeRisk").toString());
        String feedback = body.get("feedback").toString();

        testRecordService.saveFeedbackAndRisk(memberId, feedback);
        return ResponseEntity.ok("피드백/위험도 저장 완료");
    }
}

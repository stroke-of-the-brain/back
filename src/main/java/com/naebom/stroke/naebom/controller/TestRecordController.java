package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.TestRecordDto;
import com.naebom.stroke.naebom.entity.TestRecord;
import com.naebom.stroke.naebom.service.TestRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-records")
@RequiredArgsConstructor
public class TestRecordController {

    private final TestRecordService testRecordService;

    @PostMapping("/save")
    public ResponseEntity<TestRecord> saveTestRecord(@RequestBody TestRecordDto dto) {
        TestRecord savedRecord = testRecordService.saveTestRecord(dto);
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("/history/{memberId}")
    public ResponseEntity<List<TestRecordDto>> getTestHistory(@PathVariable Long memberId) {
        List<TestRecordDto> history = testRecordService.getTestHistory(memberId);
        return ResponseEntity.ok(history);
    }
}

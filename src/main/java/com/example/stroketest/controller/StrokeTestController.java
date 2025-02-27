package com.example.stroketest.controller;

import com.example.stroketest.model.TestItem;
import com.example.stroketest.model.TestResult;
import com.example.stroketest.service.StrokeTestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class StrokeTestController {

    private final StrokeTestService strokeTestService;

    public StrokeTestController(StrokeTestService strokeTestService) {
        this.strokeTestService = strokeTestService;
    }

    // 검사 결과 저장
    @PostMapping("/add/{testItemId}")
    public ResponseEntity<TestResult> saveTestResult(@PathVariable Long testItemId, @RequestParam double result) {
        TestResult savedResult = strokeTestService.saveTestResult(testItemId, result);
        return ResponseEntity.status(201).body(savedResult);
    }

    // 특정 항목에 대한 검사 결과 조회
    @GetMapping("/results/{testItemId}")
    public ResponseEntity<List<TestResult>> getTestResults(@PathVariable Long testItemId) {
        List<TestResult> results = strokeTestService.getTestResultsForItem(testItemId);
        return ResponseEntity.ok(results);
    }

    // 모든 검사 항목 조회
    @GetMapping("/items")
    public ResponseEntity<List<TestItem>> getAllTestItems() {
        List<TestItem> items = strokeTestService.getAllTestItems();
        return ResponseEntity.ok(items);
    }
}

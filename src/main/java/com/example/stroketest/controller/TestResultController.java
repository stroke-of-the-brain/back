package com.example.stroketest.controller;

import com.example.stroketest.dto.TestResultRequest;
import com.example.stroketest.dto.TestResultResponse;
import com.example.stroketest.service.TestResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    private final TestResultService testResultService;

    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

    @PostMapping
    public ResponseEntity<TestResultResponse> getTestResult(@RequestBody TestResultRequest request) {
        System.out.println("Received request: id=" + request.getId() + ", userId=" + request.getUserId() +
                ", testItemId=" + request.getTestItemId() + ", username=" + request.getUsername());
        TestResultResponse response = testResultService.getTestResult(request);
        return ResponseEntity.ok(response);
    }
}
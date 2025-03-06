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

    @PostMapping("/by-order")
    public ResponseEntity<TestResultResponse> getTestResultByOrder(@RequestBody TestResultRequest request) {
        System.out.println("Received request for test order: userId=" + request.getUserId() + ", testOrder=" + request.getTestOrder());
        TestResultResponse response = testResultService.getTestResultByOrder(request);
        return ResponseEntity.ok(response);
    }
}
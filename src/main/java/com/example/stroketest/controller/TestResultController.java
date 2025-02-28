package com.example.stroketest.controller;

import com.example.stroketest.dto.TestResultRequest;
import com.example.stroketest.dto.TestResultResponse;
import com.example.stroketest.model.TestResult;
import com.example.stroketest.service.TestResultService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
public class TestResultController {

    private final TestResultService testResultService;

    public TestResultController(TestResultService testResultService) {
        this.testResultService = testResultService;
    }

    @PostMapping
    public ResponseEntity<TestResultResponse> saveTestResult(@RequestBody TestResultRequest request) {
        TestResult testResult = testResultService.saveTestResult(request);

        TestResultResponse response = new TestResultResponse(
                testResult.getReactionTime(),
                testResult.getFacialParalysis(),
                testResult.getSpeechImpairment()
        );

        return ResponseEntity.ok(response);
    }
}

package com.example.stroketest.service;

import com.example.stroketest.model.TestResult;
import com.example.stroketest.repository.TestResultRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrokeTestService {

    private final TestResultRepository testResultRepository;

    public StrokeTestService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    public TestResult saveTestResult(TestResult testResult) {
        testResult.setStrokeProbability(calculateStrokeProbability(testResult));
        return testResultRepository.save(testResult);
    }

    public Optional<TestResult> getTestResult(Long testId) {
        return testResultRepository.findById(testId);
    }

    private double calculateStrokeProbability(TestResult testResult) {
        double probability = 0.0;

        if (testResult.getReactionTime() > 2.0) probability += 30.0;
        probability += testResult.getFacialParalysis() * 0.4;  // 최대 40점 반영
        probability += testResult.getSpeechImpairment() * 0.3; // 최대 30점 반영

        return Math.min(probability, 100.0); // 최대 100%
    }
}

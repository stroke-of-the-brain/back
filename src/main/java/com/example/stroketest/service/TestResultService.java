package com.example.stroketest.service;

import com.example.stroketest.dto.TestResultRequest;
import com.example.stroketest.dto.TestResultResponse;
import com.example.stroketest.exception.ResourceNotFoundException;
import com.example.stroketest.model.TestResult;
import com.example.stroketest.model.User;
import com.example.stroketest.repository.TestResultRepository;
import com.example.stroketest.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultService {

    private final TestResultRepository testResultRepository;
    private final UserRepository userRepository;

    public TestResultService(TestResultRepository testResultRepository, UserRepository userRepository) {
        this.testResultRepository = testResultRepository;
        this.userRepository = userRepository;
    }

    // 검사 순서로 결과 조회
    public TestResultResponse getTestResultByOrder(TestResultRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));
        List<TestResult> results = testResultRepository.findByUserOrderByCreatedAtAsc(user);

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No test results found for userId " + request.getUserId());
        }

        int order = request.getTestOrder() - 1;  // 0부터 시작하도록
        if (order < 0 || order >= results.size()) {
            throw new ResourceNotFoundException("Test order " + request.getTestOrder() + " not found for userId " + request.getUserId());
        }

        TestResult result = results.get(order);
        Double previousOverall = (request.getTestOrder() == 1) ? null : getPreviousOverallPercentage(user, result);

        return new TestResultResponse(
                result.getFacialParalysis() * 100,
                normalizeReactionTime(result.getReactionTime()),
                result.getSpeechImpairment() * 100,
                result.getCreatedAt(),
                calculateOverallPercentage(result),
                previousOverall
        );
    }

    private double calculateOverallPercentage(TestResult result) {
        double facial = result.getFacialParalysis() * 100 * 5;    // 50%
        double touch = normalizeReactionTime(result.getReactionTime()) * 3.5;  // 35%
        double speech = result.getSpeechImpairment() * 100 * 1.5; // 15%
        return (facial + touch + speech) / 10;  // 총 가중치 10으로 나누기
    }

    private Double getPreviousOverallPercentage(User user, TestResult currentResult) {
        List<TestResult> results = testResultRepository.findByUserOrderByCreatedAtAsc(user);
        int currentIndex = results.indexOf(currentResult);
        if (currentIndex <= 0) return null;  // 첫 번째 검사면 직전 결과 없음
        TestResult previousResult = results.get(currentIndex - 1);
        return calculateOverallPercentage(previousResult);
    }

    private double normalizeReactionTime(double reactionTime) {
        double maxTime = 10.0;  // 최대 10초
        return Math.min(100, Math.max(0, (1 - reactionTime / maxTime) * 100));
    }
}
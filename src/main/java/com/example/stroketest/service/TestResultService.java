package com.example.stroketest.service;

import com.example.stroketest.dto.TestResultRequest;
import com.example.stroketest.dto.TestResultResponse;
import com.example.stroketest.exception.ResourceNotFoundException;
import com.example.stroketest.model.TestItem;
import com.example.stroketest.model.TestResult;
import com.example.stroketest.model.User;
import com.example.stroketest.repository.TestItemRepository;
import com.example.stroketest.repository.TestResultRepository;
import com.example.stroketest.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TestResultService {

    private final TestResultRepository testResultRepository;
    private final TestItemRepository testItemRepository;
    private final UserRepository userRepository;

    public TestResultService(TestResultRepository testResultRepository, TestItemRepository testItemRepository, UserRepository userRepository) {
        this.testResultRepository = testResultRepository;
        this.testItemRepository = testItemRepository;
        this.userRepository = userRepository;
    }

    public TestResultResponse getTestResult(TestResultRequest request) {
        System.out.println("Received request: id=" + request.getId() + ", userId=" + request.getUserId() +
                ", testItemId=" + request.getTestItemId() + ", username=" + request.getUsername());

        System.out.println("Finding user with id: " + request.getUserId());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));
        System.out.println("Found user: " + user.getUsername());

        // username 검증 (선택적)
        if (!user.getUsername().equals(request.getUsername())) {
            System.out.println("Username mismatch: expected=" + user.getUsername() + ", received=" + request.getUsername());
        }

        System.out.println("Finding test item with id: " + request.getTestItemId());
        TestItem testItem = testItemRepository.findById(request.getTestItemId())
                .orElseThrow(() -> new ResourceNotFoundException("TestItem not found with id " + request.getTestItemId()));
        System.out.println("Found test item: " + testItem.getName());

        System.out.println("Finding test result for userId: " + request.getUserId() + ", testItemId: " + request.getTestItemId());
        TestResult testResult = testResultRepository.findByUserAndTestItem(user, testItem)
                .orElseThrow(() -> new ResourceNotFoundException("Test result not found for userId " + request.getUserId() + " and testItemId " + request.getTestItemId()));
        System.out.println("Found test result: " + testResult.getReactionTime());

        return new TestResultResponse(
                testResult.getReactionTime(),
                testResult.getFacialParalysis(),
                testResult.getSpeechImpairment()
        );
    }
}
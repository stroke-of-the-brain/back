package com.example.stroketest.service;

import com.example.stroketest.dto.TestResultRequest;
import com.example.stroketest.exception.ResourceNotFoundException;
import com.example.stroketest.model.TestItem;
import com.example.stroketest.model.TestResult;
import com.example.stroketest.model.User;
import com.example.stroketest.repository.TestItemRepository;
import com.example.stroketest.repository.TestResultRepository;
import com.example.stroketest.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Random;

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

    public TestResult saveTestResult(TestResultRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));

        TestItem testItem = testItemRepository.findById(request.getTestItemId())
                .orElseThrow(() -> new ResourceNotFoundException("TestItem not found with id " + request.getTestItemId()));

        TestResult testResult = new TestResult();
        testResult.setUser(user);
        testResult.setTestItem(testItem);
        testResult.setReactionTime(generateRandomReactionTime());
        testResult.setFacialParalysis(generateRandomFacialParalysis());
        testResult.setSpeechImpairment(generateRandomSpeechImpairment());

        return testResultRepository.save(testResult);
    }

    private double generateRandomReactionTime() {
        return new Random().nextDouble() * 10;
    }

    private double generateRandomFacialParalysis() {
        return new Random().nextDouble();
    }

    private double generateRandomSpeechImpairment() {
        return new Random().nextDouble();
    }
}


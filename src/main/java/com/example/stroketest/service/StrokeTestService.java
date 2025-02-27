package com.example.stroketest.service;

import com.example.stroketest.model.TestItem;
import com.example.stroketest.model.TestResult;
import com.example.stroketest.repository.TestItemRepository;
import com.example.stroketest.repository.TestResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrokeTestService {

    private final TestResultRepository testResultRepository;
    private final TestItemRepository testItemRepository;

    public StrokeTestService(TestResultRepository testResultRepository, TestItemRepository testItemRepository) {
        this.testResultRepository = testResultRepository;
        this.testItemRepository = testItemRepository;
    }

    public TestResult saveTestResult(Long testItemId, double result) {
        TestItem testItem = testItemRepository.findById(testItemId)
                .orElseThrow(() -> new RuntimeException("Test item not found"));
        TestResult testResult = new TestResult();
        testResult.setTestItem(testItem);
        testResult.setResult(result);
        return testResultRepository.save(testResult);
    }

    public List<TestResult> getTestResultsForItem(Long testItemId) {
        return testResultRepository.findByTestItemId(testItemId);
    }

    public List<TestItem> getAllTestItems() {
        return testItemRepository.findAll();
    }
}

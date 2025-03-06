package com.example.stroketest.repository;

import com.example.stroketest.model.TestResult;
import com.example.stroketest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUserOrderByCreatedAtAsc(User user);  // 오름차순으로 정렬
}
package com.example.stroketest.repository;

import com.example.stroketest.model.TestItem;
import com.example.stroketest.model.TestResult;
import com.example.stroketest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    Optional<TestResult> findByUserAndTestItem(User user, TestItem testItem);
}
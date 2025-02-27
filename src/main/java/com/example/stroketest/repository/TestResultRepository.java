package com.example.stroketest.repository;

import com.example.stroketest.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    // 특정 TestItem에 대한 모든 결과를 조회하는 메소드
    List<TestResult> findByTestItemId(Long testItemId);
}

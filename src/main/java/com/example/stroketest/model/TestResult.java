package com.example.stroketest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double result;  // 개별 검사 결과 값

    @ManyToOne
    @JoinColumn(name = "test_item_id", nullable = false)
    private TestItem testItem;  // 테스트 항목을 나타내는 외래 키

    public Long getId() { return id; }

    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }

    public TestItem getTestItem() { return testItem; }
    public void setTestItem(TestItem testItem) { this.testItem = testItem; }
}

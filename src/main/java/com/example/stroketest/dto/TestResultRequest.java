package com.example.stroketest.dto;

public class TestResultRequest {
    private Long userId;
    private Integer testOrder;  // 검사 순서 (1부터 시작)

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(Integer testOrder) {
        this.testOrder = testOrder;
    }
}
package com.example.stroketest.dto;

public class TestResultRequest {
    private Long id;         // 추가
    private Long userId;
    private Long testItemId;
    private String username; // 추가

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTestItemId() {
        return testItemId;
    }

    public void setTestItemId(Long testItemId) {
        this.testItemId = testItemId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
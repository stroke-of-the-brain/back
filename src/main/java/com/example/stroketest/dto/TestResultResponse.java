package com.example.stroketest.dto;

public class TestResultResponse {
    private double reactionTime;
    private double facialParalysis;
    private double speechImpairment;

    public TestResultResponse(double reactionTime, double facialParalysis, double speechImpairment) {
        this.reactionTime = reactionTime;
        this.facialParalysis = facialParalysis;
        this.speechImpairment = speechImpairment;
    }

    public double getReactionTime() {
        return reactionTime;
    }

    public double getFacialParalysis() {
        return facialParalysis;
    }

    public double getSpeechImpairment() {
        return speechImpairment;
    }
}

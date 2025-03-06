package com.example.stroketest.dto;

import java.sql.Timestamp;

public class TestResultResponse {
    private double facialPercentage;
    private double touchPercentage;
    private double speechPercentage;
    private Timestamp testDate;
    private double overallPercentage;
    private Double previousOverallPercentage;

    public TestResultResponse(double facialPercentage, double touchPercentage, double speechPercentage,
                              Timestamp testDate, double overallPercentage, Double previousOverallPercentage) {
        this.facialPercentage = facialPercentage;
        this.touchPercentage = touchPercentage;
        this.speechPercentage = speechPercentage;
        this.testDate = testDate;
        this.overallPercentage = overallPercentage;
        this.previousOverallPercentage = previousOverallPercentage;
    }

    public double getFacialPercentage() {
        return facialPercentage;
    }

    public double getTouchPercentage() {
        return touchPercentage;
    }

    public double getSpeechPercentage() {
        return speechPercentage;
    }

    public Timestamp getTestDate() {
        return testDate;
    }

    public double getOverallPercentage() {
        return overallPercentage;
    }

    public Double getPreviousOverallPercentage() {
        return previousOverallPercentage;
    }
}
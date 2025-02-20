package com.example.stroketest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double reactionTime;
    private int facialParalysis; // 0~100 (안면 마비 위험도)
    private int speechImpairment; // 0~100 (발음 장애 위험도)
    private double strokeProbability;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public double getReactionTime() { return reactionTime; }
    public void setReactionTime(double reactionTime) { this.reactionTime = reactionTime; }

    public int getFacialParalysis() { return facialParalysis; }
    public void setFacialParalysis(int facialParalysis) { this.facialParalysis = facialParalysis; }

    public int getSpeechImpairment() { return speechImpairment; }
    public void setSpeechImpairment(int speechImpairment) { this.speechImpairment = speechImpairment; }

    public double getStrokeProbability() { return strokeProbability; }
    public void setStrokeProbability(double strokeProbability) { this.strokeProbability = strokeProbability; }
}

package com.example.stroketest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_item_id", nullable = false)
    private TestItem testItem;

    private double reactionTime;
    private double facialParalysis;
    private double speechImpairment;

    @Column(name = "created_at", updatable = false, insertable = false)
    private java.sql.Timestamp createdAt;

    public double getReactionTime() {
        return reactionTime;
    }

    public double getFacialParalysis() {
        return facialParalysis;
    }

    public double getSpeechImpairment() {
        return speechImpairment;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTestItem(TestItem testItem) {
        this.testItem = testItem;
    }

    public void setReactionTime(double reactionTime) {
        this.reactionTime = reactionTime;
    }

    public void setFacialParalysis(double facialParalysis) {
        this.facialParalysis = facialParalysis;
    }

    public void setSpeechImpairment(double speechImpairment) {
        this.speechImpairment = speechImpairment;
    }
}
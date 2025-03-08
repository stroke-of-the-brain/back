package com.naebom.stroke.naebom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "test_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate testDate;

    private Double faceTestScore;
    private Double speechTestScore;
    private Double fingerTestScore;

    @Column(nullable = false)
    private Boolean strokeRisk;
}

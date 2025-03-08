package com.naebom.stroke.naebom.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestRecordDto {
    private Long userId;
    private LocalDate testDate;
    private Double faceTestScore;
    private Double speechTestScore;
    private Double fingerTestScore;
    private Boolean strokeRisk;
}

package com.naebom.stroke.naebom.service;

import com.naebom.stroke.naebom.dto.TestRecordDto;
import com.naebom.stroke.naebom.entity.Member;
import com.naebom.stroke.naebom.entity.TestRecord;
import com.naebom.stroke.naebom.repository.MemberRepository;
import com.naebom.stroke.naebom.repository.TestRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestRecordService {

    private final TestRecordRepository testRecordRepository;
    private final MemberRepository memberRepository;

    //검사 결과 저장 (testCount, avgRiskScore 자동 추가, feedback은 프론트에서 받음)
    public TestRecordDto saveTestRecord(TestRecordDto dto) {
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID"));

        // 검사 횟수 자동 증가
        int testCount = testRecordRepository.countByMemberId(dto.getMemberId()) + 1;

        // 평균 뇌졸중 위험 확률 계산
        /*Double avgRiskScore = 100 -(dto.getFaceTestScore() + dto.getSpeechTestScore() + dto.getFingerTestScore()) / 3;*/
        Double avgRiskScore = Math.round((100 - ((dto.getFaceTestScore() + dto.getSpeechTestScore() + dto.getFingerTestScore()) / 3)) * 10.0) / 10.0;


        TestRecord testRecord = TestRecord.builder()
                .member(member)
                .testDate(dto.getTestDate())
                .faceTestScore(dto.getFaceTestScore())
                .speechTestScore(dto.getSpeechTestScore())
                .fingerTestScore(dto.getFingerTestScore())
                .strokeRisk(dto.getStrokeRisk())
                .testCount(testCount)         // 추가된 필드
                .avgRiskScore(avgRiskScore)   // 추가된 필드
                .feedback(dto.getFeedback())  // 프론트에서 받은 feedback 저장
                .build();

        testRecordRepository.save(testRecord);

        return new TestRecordDto(
                dto.getMemberId(),
                dto.getTestDate(),
                dto.getFaceTestScore(),
                dto.getSpeechTestScore(),
                dto.getFingerTestScore(),
                dto.getStrokeRisk(),
                testCount,
                dto.getFeedback(), // 프론트에서 받은 feedback 전달
                avgRiskScore
        );
    }

    //모든 검사 기록 조회
    public List<TestRecordDto> getTestHistory(Long memberId) {
        List<TestRecord> records = testRecordRepository.findByMemberIdOrderByTestDateDesc(memberId);
        return convertToDto(records);
    }

    //최근 2개의 검사 기록 조회
    public List<TestRecordDto> getRecentTwoTestRecords(Long memberId) {
        List<TestRecord> records = testRecordRepository.findRecentTestRecords(memberId);
        return convertToDto(records);
    }

    //중복된 변환 로직을 메서드로 따로 분리
    private List<TestRecordDto> convertToDto(List<TestRecord> records) {
        return records.stream()
                .map(record -> new TestRecordDto(
                        record.getMember().getId(),
                        record.getTestDate(),
                        record.getFaceTestScore(),
                        record.getSpeechTestScore(),
                        record.getFingerTestScore(),
                        record.getStrokeRisk(),
                        record.getTestCount(),
                        record.getFeedback(),
                        record.getAvgRiskScore()
                ))
                .collect(Collectors.toList());
    }
    //최근 2개의 검사 기록 조회 (간단한 데이터만 반환)
    public List<Map<String, Object>> getRecentTwoSimpleTestRecords(Long memberId) {
        List<TestRecord> records = testRecordRepository.findRecentTestRecords(memberId);
        return convertToSimpleDto(records);
    }

//모든 검사 기록 조회 (간단한 데이터만 반환)
public List<Map<String, Object>> getSimpleTestHistory(Long memberId) {
    List<TestRecord> records = testRecordRepository.findByMemberIdOrderByTestDateDesc(memberId);
    return convertToSimpleDto(records);
}
    private List<Map<String, Object>> convertToSimpleDto(List<TestRecord> records) {
        return records.stream()
                .map(record -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("memberId", record.getMember().getId());
                    result.put("testDate", record.getTestDate());
                    if (record.getAvgRiskScore() != null) {
                        result.put("avgRiskScore", Math.round(record.getAvgRiskScore() * 10.0) / 10.0);
                    }
                    if (record.getFeedback() != null) {
                        result.put("feedback", record.getFeedback());
                    }
                    if (record.getTestCount() != null) {
                        result.put("testCount", record.getTestCount()); // test_count 추가
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }
    /*//fingerTestScore(반응 속도 점수)만 저장하는 메서드 추가
    public void saveFingerTestScore(Long memberId, Double fingerTestScore) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID"));

        TestRecord testRecord = TestRecord.builder()
                .member(member)
                .testDate(LocalDate.now()) // 현재 날짜 자동 저장
                .fingerTestScore(fingerTestScore) // 반응 속도 점수 저장
                .build();

        testRecordRepository.save(testRecord);
    }*/
   /* public void saveAiScore(Long memberId, Double score) {
        //memberId를 이용해 Member 엔티티 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID"));

        //빌더 패턴에서 member 객체를 설정
        TestRecord testRecord = TestRecord.builder()
                .member(member)  //memberId가 아니라 member 객체를 전달해야 함
                .testDate(LocalDate.now())
                .faceTestScore(score)
                .strokeRisk(false) // 기본 값 설정
                .testCount(1)  // 기본 값 설정
                .build();

        testRecordRepository.save(testRecord);
        System.out.println("AI Score saved: " + score);
    }*/
}

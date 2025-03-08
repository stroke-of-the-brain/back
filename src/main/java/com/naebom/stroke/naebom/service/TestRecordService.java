package com.naebom.stroke.naebom.service;

import com.naebom.stroke.naebom.dto.TestRecordDto;
import com.naebom.stroke.naebom.entity.Member;
import com.naebom.stroke.naebom.entity.TestRecord;
import com.naebom.stroke.naebom.repository.MemberRepository;
import com.naebom.stroke.naebom.repository.TestRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestRecordService {

    private final TestRecordRepository testRecordRepository;
    private final MemberRepository memberRepository;

    public TestRecord saveTestRecord(TestRecordDto dto) {
        Member member = memberRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 ID"));

        TestRecord testRecord = TestRecord.builder()
                .member(member)
                .testDate(dto.getTestDate())  // 프론트에서 testDate 전달 필요
                .faceTestScore(dto.getFaceTestScore())
                .speechTestScore(dto.getSpeechTestScore())
                .fingerTestScore(dto.getFingerTestScore())
                .strokeRisk(dto.getStrokeRisk())
                .build();

        return testRecordRepository.save(testRecord);
    }

    public List<TestRecordDto> getTestHistory(Long userId) {
        List<TestRecord> records = testRecordRepository.findByMemberIdOrderByTestDateDesc(userId);
        return records.stream()
                .map(record -> new TestRecordDto(
                        record.getMember().getId(),
                        record.getTestDate(),
                        record.getFaceTestScore(),
                        record.getSpeechTestScore(),
                        record.getFingerTestScore(),
                        record.getStrokeRisk()))
                .collect(Collectors.toList());
    }
}

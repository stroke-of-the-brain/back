package com.naebom.stroke.naebom.repository;

import com.naebom.stroke.naebom.entity.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestRecordRepository extends JpaRepository<TestRecord, Long> {

    // 모든 검사 기록 조회 (최신순)
    List<TestRecord> findByMemberIdOrderByTestDateDesc(Long memberId);

    // 최근 2개의 검사 기록 조회
    @Query(value = "SELECT * FROM test_record WHERE member_id = :memberId ORDER BY test_date DESC LIMIT 2", nativeQuery = true)
    List<TestRecord> findRecentTestRecords(@Param("memberId") Long memberId);

    // ✅ 특정 사용자의 총 검사 횟수 반환 (새로운 메서드 추가)
    @Query("SELECT COUNT(t) FROM TestRecord t WHERE t.member.id = :memberId")
    int countByMemberId(@Param("memberId") Long memberId);
}

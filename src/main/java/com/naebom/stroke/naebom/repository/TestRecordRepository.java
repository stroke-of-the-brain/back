package com.naebom.stroke.naebom.repository;

import com.naebom.stroke.naebom.entity.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRecordRepository extends JpaRepository<TestRecord, Long> {
    List<TestRecord> findByMemberIdOrderByTestDateDesc(Long memberId);
}

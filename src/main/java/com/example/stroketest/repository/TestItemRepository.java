package com.example.stroketest.repository;


import com.example.stroketest.model.TestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestItemRepository extends JpaRepository<TestItem, Long> {
    // 필요한 추가적인 쿼리 메소드들 작성 가능함:)
    // 추후 추가예정
}

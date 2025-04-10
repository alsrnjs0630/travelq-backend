package com.travelq.backend.repository;

import com.travelq.backend.entity.RecommendCmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendCmtRepository extends JpaRepository<RecommendCmt, Long> {
}

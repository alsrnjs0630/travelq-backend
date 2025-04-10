package com.travelq.backend.repository;

import com.travelq.backend.entity.AskCmt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AskCmtRepository extends JpaRepository<AskCmt, Long> {
}

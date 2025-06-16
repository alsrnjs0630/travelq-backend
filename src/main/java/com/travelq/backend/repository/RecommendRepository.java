package com.travelq.backend.repository;

import com.travelq.backend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendRepository extends JpaRepository<Recommend, Long>, JpaSpecificationExecutor<Recommend> {
}

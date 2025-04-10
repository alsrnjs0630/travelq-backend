package com.travelq.backend.repository;

import com.travelq.backend.entity.RecommendImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendImgRepository extends JpaRepository<RecommendImg, Long> {
}

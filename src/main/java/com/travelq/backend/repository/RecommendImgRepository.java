package com.travelq.backend.repository;

import com.travelq.backend.entity.RecommendImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendImgRepository extends JpaRepository<RecommendImg, Long> {
    List<RecommendImg> findByRecommendId(Long recommendId);
    Optional<RecommendImg> findByImageName(String imageName);
}

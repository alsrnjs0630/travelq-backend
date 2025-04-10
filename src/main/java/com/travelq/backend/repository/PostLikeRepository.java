package com.travelq.backend.repository;

import com.travelq.backend.entity.Member;
import com.travelq.backend.entity.PostLike;
import com.travelq.backend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findAllByMember(Member member);
    Optional<PostLike> findByRecommendAndMember(Recommend recommend, Member member);
}

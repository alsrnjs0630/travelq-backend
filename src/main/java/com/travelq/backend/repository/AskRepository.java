package com.travelq.backend.repository;

import com.travelq.backend.entity.Ask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AskRepository extends JpaRepository<Ask, Long>, JpaSpecificationExecutor<Ask> {
}

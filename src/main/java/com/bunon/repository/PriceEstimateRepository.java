package com.bunon.repository;

import com.bunon.model.PriceEstimate;
import com.bunon.model.Weaver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceEstimateRepository extends JpaRepository<PriceEstimate, Long> {
    List<PriceEstimate> findByWeaverOrderByCreatedAtDesc(Weaver weaver);
}

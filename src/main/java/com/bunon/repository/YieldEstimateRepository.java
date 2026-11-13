package com.bunon.repository;

import com.bunon.model.Loom;
import com.bunon.model.YieldEstimate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YieldEstimateRepository extends JpaRepository<YieldEstimate, Long> {
    List<YieldEstimate> findByLoomOrderByCreatedAtDesc(Loom loom);
}

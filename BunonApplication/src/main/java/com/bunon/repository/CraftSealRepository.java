package com.bunon.repository;

import com.bunon.model.CraftSeal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CraftSealRepository extends JpaRepository<CraftSeal, Long> {
    Optional<CraftSeal> findFirstByProductIdAndStatus(Long productId, String status);
    Optional<CraftSeal> findFirstByWeaverIdAndStatus(Long weaverId, String status);
}

package com.bunon.repository;

import com.bunon.model.CraftSeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CraftSealRepository extends JpaRepository<CraftSeal, Long> {
    List<CraftSeal> findByRevokedFalseOrderByAssignedAtDesc();
}

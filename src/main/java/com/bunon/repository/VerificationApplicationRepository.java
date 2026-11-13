package com.bunon.repository;

import com.bunon.model.VerificationApplication;
import com.bunon.model.VerificationStatus;
import com.bunon.model.Weaver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationApplicationRepository extends JpaRepository<VerificationApplication, Long> {
    List<VerificationApplication> findByStatusOrderBySubmittedAtAsc(VerificationStatus status);
    Optional<VerificationApplication> findTopByWeaverOrderBySubmittedAtDesc(Weaver weaver);
}

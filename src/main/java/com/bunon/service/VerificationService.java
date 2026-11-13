package com.bunon.service;

import com.bunon.model.User;
import com.bunon.model.VerificationApplication;
import com.bunon.model.VerificationStatus;
import com.bunon.repository.VerificationApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * F16 - Weaver Onboarding & NID Verification (admin side).
 * OWNER: Member 1
 */
@Service
public class VerificationService {

    private final VerificationApplicationRepository applicationRepository;

    public VerificationService(VerificationApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<VerificationApplication> pendingApplications() {
        return applicationRepository.findByStatusOrderBySubmittedAtAsc(VerificationStatus.PENDING);
    }

    public VerificationApplication get(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    @Transactional
    public void approve(Long id, User admin) {
        // TODO F16: status=APPROVED, weaver.verified=true, reviewedAt/reviewedBy
    }

    @Transactional
    public void reject(Long id, User admin, String note) {
        // TODO F16: status=REJECTED + adminNote (note required)
    }

    @Transactional
    public void requestCorrection(Long id, User admin, String note) {
        // TODO F16: status=CORRECTION_REQUESTED + adminNote; weaver can then resubmit (see AuthController TODO)
    }
}

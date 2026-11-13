package com.bunon.service;

import com.bunon.model.User;
import com.bunon.model.VerificationApplication;
import com.bunon.model.VerificationStatus;
import com.bunon.repository.VerificationApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        review(id, admin, VerificationStatus.APPROVED, null);
    }

    @Transactional
    public void reject(Long id, User admin, String note) {
        requireNote(note);
        review(id, admin, VerificationStatus.REJECTED, note);
    }

    @Transactional
    public void requestCorrection(Long id, User admin, String note) {
        requireNote(note);
        review(id, admin, VerificationStatus.CORRECTION_REQUESTED, note);
    }

    private void review(Long id, User admin, VerificationStatus status, String note) {
        VerificationApplication app = get(id);
        if (app.getStatus() != VerificationStatus.PENDING) {
            throw new IllegalStateException("This application has already been reviewed");
        }
        app.setStatus(status);
        app.setAdminNote(note);
        app.setReviewedAt(LocalDateTime.now());
        app.setReviewedBy(admin);
        app.getWeaver().setVerified(status == VerificationStatus.APPROVED);
        applicationRepository.save(app);
    }

    private void requireNote(String note) {
        if (note == null || note.isBlank()) {
            throw new IllegalArgumentException("A note is required");
        }
    }
}
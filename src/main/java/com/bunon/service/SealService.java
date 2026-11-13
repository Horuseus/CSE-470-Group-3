package com.bunon.service;

import com.bunon.model.CraftSeal;
import com.bunon.model.User;
import com.bunon.repository.CraftSealRepository;
import com.bunon.repository.ProductRepository;
import com.bunon.repository.WeaverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * F17 - Authentic Handloom Craft Seal Assignment.
 * OWNER: Member 4
 */
@Service
public class SealService {

    private final CraftSealRepository sealRepository;
    private final WeaverRepository weaverRepository;
    private final ProductRepository productRepository;

    public SealService(CraftSealRepository sealRepository, WeaverRepository weaverRepository,
                       ProductRepository productRepository) {
        this.sealRepository = sealRepository;
        this.weaverRepository = weaverRepository;
        this.productRepository = productRepository;
    }

    public List<CraftSeal> activeSeals() {
        return sealRepository.findByRevokedFalseOrderByAssignedAtDesc();
    }

    @Transactional
    public CraftSeal assignToWeaver(Long weaverId, String evidenceNote, User admin) {
        // TODO F17: only VERIFIED weavers may get a seal; generate unique sealCode (BUN-<year>-<0001>)
        throw new UnsupportedOperationException("TODO F17");
    }

    @Transactional
    public CraftSeal assignToProduct(Long productId, String evidenceNote, User admin) {
        // TODO F17: product's weaver must be verified; no duplicate active seal for same product
        throw new UnsupportedOperationException("TODO F17");
    }

    @Transactional
    public void revoke(Long sealId) {
        // TODO F17
    }
}

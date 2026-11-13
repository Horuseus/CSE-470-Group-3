package com.bunon.service;

import com.bunon.model.AvailabilityType;
import com.bunon.model.Product;
import com.bunon.model.Weaver;
import com.bunon.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * F5 - Availability & Pre-order Management (+ basic product creation used by F4/F5/F17).
 * OWNER: Member 3
 */
@Service
public class AvailabilityService {

    private final ProductRepository productRepository;

    public AvailabilityService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> productsOf(Weaver weaver) {
        return productRepository.findByWeaver(weaver);
    }

    public Product createProduct(Weaver weaver, String name, String category, java.math.BigDecimal price) {
        // TODO F5: basic product creation so other features have data (image upload can wait for Sprint 2)
        throw new UnsupportedOperationException("TODO F5");
    }

    public Product updateAvailability(Long productId, Weaver owner, int stockQuantity,
                                      AvailabilityType type, LocalDate expectedCompletion) {
        // TODO F5: owner check; IN_STOCK needs stock>0; PRE_ORDER needs a future expectedCompletion date
        throw new UnsupportedOperationException("TODO F5");
    }
}

package com.bunon.repository;

import com.bunon.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByWeaverId(Long weaverId);
    List<Product> findByWeaverIdAndCategory(Long weaverId, String category);
    List<Product> findByLoomId(Long loomId);
}

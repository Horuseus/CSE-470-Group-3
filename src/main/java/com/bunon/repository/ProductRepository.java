package com.bunon.repository;

import com.bunon.model.Product;
import com.bunon.model.Weaver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByWeaver(Weaver weaver);
}

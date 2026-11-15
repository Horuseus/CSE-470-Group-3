package com.bunon.repository;

import com.bunon.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

/**
 * JpaRepository gives us save(), findById(), findAll(), delete()... for free.
 * Method names like findByWeaverId are turned into SQL automatically by Spring.
 */
public interface WeaverRepository extends JpaRepository<Weaver, Long> {
    Optional<Weaver> findByUserEmail(String email);
}

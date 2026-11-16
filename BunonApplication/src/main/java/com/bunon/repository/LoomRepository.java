package com.bunon.repository;

import com.bunon.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;


public interface LoomRepository extends JpaRepository<Loom, Long> {
    List<Loom> findByWeaverId(Long weaverId);
}

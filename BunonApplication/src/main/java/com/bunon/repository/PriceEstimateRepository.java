package com.bunon.repository;

import com.bunon.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PriceEstimateRepository extends JpaRepository<PriceEstimate, Long> {
    List<PriceEstimate> findTop5ByWeaverIdOrderByIdDesc(Long weaverId);   // this weaver's 5 newest
}

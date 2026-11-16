package com.bunon.repository;

import com.bunon.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

/**
 * JpaRepository gives us save(), findById(), findAll(), delete()... for free.
 * Method names like findByWeaverId are turned into SQL automatically by Spring.
 */
public interface PassportEntryRepository extends JpaRepository<PassportEntry, Long> {
    Optional<PassportEntry> findTopByPassportIdOrderByIdDesc(Long passportId);   // the newest entry
    List<PassportEntry> findByPassportIdOrderByIdAsc(Long passportId);           // all entries, oldest first
}

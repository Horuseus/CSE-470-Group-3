package com.bunon.repository;

import com.bunon.model.Loom;
import com.bunon.model.Weaver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoomRepository extends JpaRepository<Loom, Long> {
    List<Loom> findByWeaver(Weaver weaver);
}

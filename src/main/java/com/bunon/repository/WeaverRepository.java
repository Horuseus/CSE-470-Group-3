package com.bunon.repository;

import com.bunon.model.User;
import com.bunon.model.Weaver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeaverRepository extends JpaRepository<Weaver, Long> {
    Optional<Weaver> findByUser(User user);
    boolean existsByNidNumber(String nidNumber);
}

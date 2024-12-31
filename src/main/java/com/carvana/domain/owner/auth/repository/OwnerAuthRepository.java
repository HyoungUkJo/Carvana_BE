package com.carvana.domain.owner.auth.repository;

import com.carvana.domain.owner.auth.entity.OwnerAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerAuthRepository extends JpaRepository<OwnerAuth, Long> {
    boolean existsByEmail(String email);

    Optional<OwnerAuth> findByEmail(String email);

    boolean findByPassword(String password);
}

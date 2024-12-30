package com.carvana.domain.user.auth.repository;

import com.carvana.domain.user.auth.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    boolean existsByEmail(String email);

    Optional<Auth> findByEmail(String email);

    boolean findByPassword(String password);
}

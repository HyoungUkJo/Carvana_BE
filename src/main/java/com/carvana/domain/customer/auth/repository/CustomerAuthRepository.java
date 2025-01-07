package com.carvana.domain.customer.auth.repository;

import com.carvana.domain.customer.auth.entity.CustomerAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerAuthRepository extends JpaRepository<CustomerAuth, Long> {
    boolean existsByEmail(String email);

    Optional<CustomerAuth> findByEmail(String email);

    boolean findByPassword(String password);
}

package com.carvana.global.notification.repository;

import com.carvana.global.notification.entity.CustomerFcmToken;
import com.carvana.global.notification.entity.OwnerFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerFcmTokenRepository extends JpaRepository<CustomerFcmToken, Long> {
    Optional<CustomerFcmToken> findByCustomerMemberId(Long customerId);
}

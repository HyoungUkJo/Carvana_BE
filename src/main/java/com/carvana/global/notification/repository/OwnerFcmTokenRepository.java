package com.carvana.global.notification.repository;

import com.carvana.global.notification.entity.OwnerFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerFcmTokenRepository extends JpaRepository<OwnerFcmToken, Long> {
    Optional<OwnerFcmToken> findByOwnerMemberId(Long ownerId);
}

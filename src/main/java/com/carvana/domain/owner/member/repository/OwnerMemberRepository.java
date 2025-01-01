package com.carvana.domain.owner.member.repository;

import com.carvana.domain.owner.member.entity.OwnerMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerMemberRepository extends JpaRepository<OwnerMember, Long> {
    Optional<OwnerMember> findById(Long id);

}

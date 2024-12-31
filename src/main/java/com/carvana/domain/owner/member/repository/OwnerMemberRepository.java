package com.carvana.domain.owner.member.repository;

import com.carvana.domain.owner.member.entity.OwnerMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerMemberRepository extends JpaRepository<OwnerMember, Long> {

}

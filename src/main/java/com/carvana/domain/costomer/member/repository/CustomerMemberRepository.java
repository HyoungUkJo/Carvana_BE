package com.carvana.domain.costomer.member.repository;

import com.carvana.domain.costomer.member.entity.CustomerMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerMemberRepository extends JpaRepository<CustomerMember, Long> {

}

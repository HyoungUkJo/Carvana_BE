package com.carvana.domain.customer.member.repository;

import com.carvana.domain.customer.member.entity.CustomerMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerMemberRepository extends JpaRepository<CustomerMember, Long> {

}

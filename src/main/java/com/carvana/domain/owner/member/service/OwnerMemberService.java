package com.carvana.domain.owner.member.service;

import com.carvana.domain.owner.member.repository.OwnerMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerMemberService {

    private final OwnerMemberRepository ownerMemberRepository;
}

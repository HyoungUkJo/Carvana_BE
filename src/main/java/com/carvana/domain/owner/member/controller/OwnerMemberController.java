package com.carvana.domain.owner.member.controller;

import com.carvana.domain.owner.member.service.OwnerMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/owner")
@RequiredArgsConstructor
public class OwnerMemberController {

    private OwnerMemberService ownerMemberService;

}

package com.carvana.domain.store.carwash.service;

import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.owner.member.repository.OwnerMemberRepository;
import com.carvana.domain.store.carwash.dto.RegisterCarWashMenuRequestDto;
import com.carvana.domain.store.carwash.dto.RegisterCarWashMenuResponseDto;
import com.carvana.domain.store.carwash.dto.RegisterCarWashRequestDto;
import com.carvana.domain.store.carwash.dto.RegisterCarWashResponseDto;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import com.carvana.domain.store.carwash.repository.CarWashMenuRepository;
import com.carvana.domain.store.carwash.repository.CarWashRepository;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarWashService {
    private final CarWashRepository carWashRepository;
    private final OwnerMemberRepository ownerMemberRepository;
    private final CarWashMenuRepository carWashMenuRepository;
    public RegisterCarWashResponseDto registerCarWash(RegisterCarWashRequestDto registerCarWashRequestDto, Long ownerId) {
        // 실제 오너가 있는지 검증
        OwnerMember ownerMember = ownerMemberRepository.findById(ownerId).orElseThrow(() -> new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다."));
        // 추후 중복되는 주소 또는 번호로 검증해야할듯?
        CarWash carWash = CarWash.builder()
            .name(registerCarWashRequestDto.getName())
            .phone(registerCarWashRequestDto.getPhone())
            .address(registerCarWashRequestDto.getAddress())
            .businessHours(registerCarWashRequestDto.getBusinessHours())
            .build();

        ownerMember.addCarWash(carWash);
        carWashRepository.save(carWash);

        // 이거 빌더 패턴으로 할지 다시 생각해볼 필요 있음
        RegisterCarWashResponseDto registerCarWashResponseDto = new RegisterCarWashResponseDto();
        registerCarWashResponseDto.setCarWashId(carWash.getId());

        return registerCarWashResponseDto;
    }
    public RegisterCarWashMenuResponseDto registerCarWashMenu(Long carWashId, RegisterCarWashMenuRequestDto registerCarWashMenuRequestDto) {
        // 검증 방법 추후 필요.
        // 요청하는 오너의 가게가 맞는지 권한 확인 필요. -> 이건 컨트롤러에서?
        CarWash carWash = carWashRepository.findById(carWashId).orElseThrow(/*여기에 에러처리필요*/);

        CarWashMenu carWashMenu = CarWashMenu.builder()
            .menuName(registerCarWashMenuRequestDto.getMenuName())
            .menuDescription(registerCarWashMenuRequestDto.getMenuDescription())
            .duration(registerCarWashMenuRequestDto.getDuration())
            .price(registerCarWashMenuRequestDto.getPrice())
            .build();
        carWash.addMenu(carWashMenu);

        return RegisterCarWashMenuResponseDto.builder().carWashId(carWash.getId()).carWashMenuId(carWashMenu.getId()).build();
    }
}

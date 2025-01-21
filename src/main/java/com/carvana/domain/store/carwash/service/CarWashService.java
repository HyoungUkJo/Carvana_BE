package com.carvana.domain.store.carwash.service;

import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.owner.member.repository.OwnerMemberRepository;
import com.carvana.domain.reservation.dto.MonthlyStatsDto;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.reservation.repository.ReservationRepository;
import com.carvana.domain.review.repository.ReviewRepository;
import com.carvana.domain.store.carwash.dto.*;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashBusinessTarget;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import com.carvana.domain.store.carwash.repository.CarWashBusinessTargetRepository;
import com.carvana.domain.store.carwash.repository.CarWashMenuRepository;
import com.carvana.domain.store.carwash.repository.CarWashRepository;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CarWashService {
    private final CarWashRepository carWashRepository;
    private final OwnerMemberRepository ownerMemberRepository;
    private final CarWashMenuRepository carWashMenuRepository;
    private final CarWashBusinessTargetRepository carWashBusinessTargetRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    public RegisterCarWashResponseDto registerCarWash(RegisterCarWashRequestDto registerCarWashRequestDto) {
        // 실제 오너가 있는지 검증
        OwnerMember ownerMember = ownerMemberRepository.findById(registerCarWashRequestDto.getOwnerId()).orElseThrow(() -> new IncorrectEmailPasswordException("아이디 또는 비밀번호가 틀립니다."));
        // 추후 중복되는 주소 또는 번호로 검증해야할듯?
        CarWash carWash = CarWash.builder()
            .name(registerCarWashRequestDto.getName())
            .phone(registerCarWashRequestDto.getPhone())
            .address(registerCarWashRequestDto.getAddress())
            .businessHours(registerCarWashRequestDto.getBusinessHours())
            .thumbnailImgUrl(registerCarWashRequestDto.getThumbnailImgUrl())
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

    // 세차장 프로필
    public CarWashProfileResponseDto getCarWashProfile(Long carWasId){
        CarWash carWash = carWashRepository.findById(carWasId).orElseThrow(() -> new EntityNotFoundException("해당하는 세차장이 없습니다."));
        OwnerMember ownerMember = carWash.getOwnerMember();

        return CarWashProfileResponseDto.builder()
            .name(carWash.getName())
            .businessNumber(ownerMember.getBusinessNumber())
            .address(carWash.getAddress())
            .bayCount(carWash.getBayCount())
            .phone(carWash.getPhone())
            .businessHours(carWash.getBusinessHours())
            .build();
    }


    // 세차장 프로필 수정
    public CarWashProfileResponseDto updateCarWashProfile(Long carWashId,CarWashProfileUpdateRequestDto dto) {
        CarWash carWash = carWashRepository.findById(carWashId).orElseThrow(() -> new EntityNotFoundException("해당하는 세차장이 없습니다."));
        carWash.updateCarWash(dto);

        return CarWashProfileResponseDto.builder()
            .name(carWash.getName())
            .businessNumber(carWash.getOwnerMember().getBusinessNumber())
            .address(carWash.getAddress())
            .bayCount(carWash.getBayCount())
            .phone(carWash.getPhone())
            .businessHours(carWash.getBusinessHours())
            .build();
    }
    public CarWashMonthlyStatsDto getMonthlyStats(Long carWashId) {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        // 1. 예약리스트를가지고온다.
        List<Reservation> reservations = reservationRepository.findByCarWashIdAndStatusAndReservationDateTimeBetweenOrderByReservationDateTime(
            carWashId,
            ReservationStatus.COMPLETED,
            startOfMonth,
            endOfMonth);

        // 2. 리뷰를 가지고온다.
        int reviewCount = reviewRepository.countByCarWashIdAndCreatedAtBetween(carWashId, startOfMonth, endOfMonth);

        // 3. 목표치를 가지고온다.
        CarWashBusinessTarget carWashBusinessTarget = carWashBusinessTargetRepository.findByCarWashId(carWashId).orElseGet(() -> CarWashBusinessTarget.builder()
            .monthlyWorkloadTarget(0)
            .monthlyRevenueTarget(0)
            .monthlyReviewTarget(0)
            .build());;

        //4. 예약 리스트에서 예약 수와 매출액을 리뷰와 함께 return한다.
        return CarWashMonthlyStatsDto.builder()
            .totalWorkload(reservations.size())
            .totalRevenue(reservations.stream()
                .mapToInt(revenue -> revenue.calculateTotalPrice())
                .sum())
            .totalReviews(reviewCount)
            .targetWorkload(carWashBusinessTarget.getMonthlyWorkloadTarget())
            .targetRevenue(carWashBusinessTarget.getMonthlyRevenueTarget())
            .targetReviews(carWashBusinessTarget.getMonthlyReviewTarget())
            .build();
    }

    // 세차장 월간 작업량, 매출, 리뷰 목표 설정
    @Transactional
    public CarWashBusinessTarget setCarWashTarget(SetCarWashTargetRequestDto dto) {
        CarWash carWash = carWashRepository.findById(dto.getCarWashId()).orElseThrow(() -> {
            return new EntityNotFoundException("해당하는 세차장이 없습니다.");
        });

        return carWashBusinessTargetRepository.findByCarWashId(carWash.getId()).orElseGet(() -> CarWashBusinessTarget.builder()
            .monthlyRevenueTarget(dto.getTargetRevenue())
            .monthlyWorkloadTarget(dto.getTargetWorkload())
            .monthlyReviewTarget(dto.getTargetReviews())
            .build());
    }

    // 업체 상세 정보 (업체정보, 메뉴 등)
    public CarWashInfoResponseDto getCarWashInfo(Long carWashId) {
        CarWash carWash = carWashRepository.findById(carWashId).orElseThrow(() -> new EntityNotFoundException("해당하는 세차장이 없습니다."));

        List<CarWashMenuDto> carWashMenuDtoList = carWash.getMenus().stream()
            .map(CarWashMenuDto::new)
            .toList();

        return CarWashInfoResponseDto.builder()
            .id(carWash.getId())
            .name(carWash.getName())
            .address(carWash.getAddress())
            .phone(carWash.getPhone())
            .businessHours(carWash.getBusinessHours())
            .bayCount(carWash.getBayCount())
            .thumbnailImgUrl(carWash.getThumbnailImgUrl())
            .carWashMenus(carWashMenuDtoList)
            .build();
    }

}

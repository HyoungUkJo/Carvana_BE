package com.carvana.domain.store.carwash.service;

import com.carvana.domain.owner.auth.entity.OwnerAuth;
import com.carvana.domain.owner.auth.repository.OwnerAuthRepository;
import com.carvana.domain.owner.member.entity.OwnerMember;
import com.carvana.domain.owner.member.repository.OwnerMemberRepository;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.reservation.repository.ReservationRepository;
import com.carvana.domain.review.repository.ReviewRepository;
import com.carvana.domain.store.carwash.dto.*;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashBusinessTarget;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import com.carvana.domain.store.carwash.repository.CarWashBusinessTargetRepository;
import com.carvana.domain.store.carwash.repository.CarWashRepository;
import com.carvana.global.exception.custom.IncorrectEmailPasswordException;
import com.carvana.global.storage.service.PresignedUrlService;
import com.carvana.global.storage.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CarWashService {
    private final CarWashRepository carWashRepository;
    private final OwnerMemberRepository ownerMemberRepository;
    private final OwnerAuthRepository ownerAuthRepository;
    private final CarWashBusinessTargetRepository carWashBusinessTargetRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final PresignedUrlService presignedUrlService;
    private final StorageService storageService;

    public RegisterCarWashResponseDto registerCarWash(RegisterCarWashRequestDto registerCarWashRequestDto) {
        // JWT를 이용해 실제 오너가 있는지 검증
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        OwnerAuth auth = ownerAuthRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        OwnerMember ownerMember = auth.getOwnerMember();

        // uuid 생성
        String carWashUUID = UUID.randomUUID().toString();

        // 키를 null로 미리 선언 -> 이미지가 없을 경우를 대비
        String thumbnailKey = null;
        System.out.println("이미지 디버깅:"+registerCarWashRequestDto.getThumbnailImg());


        // 이미지를 넣지 않았을때를 대비한 조건문 처리
        if(registerCarWashRequestDto.getThumbnailImg() != null
            && !registerCarWashRequestDto.getThumbnailImg().isEmpty()){
            // 사진 등록
            System.out.println("이미지 디버깅:"+registerCarWashRequestDto.getThumbnailImg());

            thumbnailKey = storageService.uploadFile(registerCarWashRequestDto.getThumbnailImg(),
                "carwash_thumbnail",
                carWashUUID);
        }

        System.out.println("thumbnailKey: "+thumbnailKey);


        // 추후 중복되는 주소 또는 번호로 검증해야할듯?
        CarWash carWash = CarWash.builder()
            .name(registerCarWashRequestDto.getName())
            .bayCount(registerCarWashRequestDto.getBayCount())
            .phone(registerCarWashRequestDto.getPhone())
            .address(registerCarWashRequestDto.getAddress())
            .businessHours(registerCarWashRequestDto.getBusinessHours())
            .thumbnailImgKey(thumbnailKey)
            .uuid(carWashUUID)
            .build();

        ownerMember.addCarWash(carWash);
        carWashRepository.save(carWash);

        return RegisterCarWashResponseDto.builder()
            .carWashId(carWash.getId())
            .uuid(carWash.getUuid())
            .address(carWash.getAddress())
            .phone(carWash.getPhone())
            .businessHours(carWash.getBusinessHours())
            .bayCount(carWash.getBayCount())
            .thumbnailImgUrl(
                presignedUrlService.generatePresignedUrl(carWash.getThumbnailImgKey(),60)
            )
            .build();
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
            .thumbnailImgUrl(carWash.getThumbnailImgKey())
            .build();
    }


    // 세차장 프로필 수정
    public CarWashProfileResponseDto updateCarWashProfile(Long carWashId,CarWashProfileUpdateRequestDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        OwnerAuth auth = ownerAuthRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 오너의 carWash List와 같이 검증하는 로직 작성
        OwnerMember ownerMember = auth.getOwnerMember();
        List<CarWash> carWashList = ownerMember.getCarWashes();

        // 임시
        CarWash carWash = carWashRepository.findById(dto.getCarWashId())
            .orElseThrow(() -> new EntityNotFoundException("세차장을 찾을 수 없음"));

        String thumbnailImgKey = carWash.getThumbnailImgKey();


        if(dto.getThumbnailImg()!=null && !dto.getThumbnailImg().isEmpty()) {
            System.out.println("여기까지는 들어옴");
            // 기존 썸네일이 있는 경우 삭제
            if(thumbnailImgKey != null && !thumbnailImgKey.isEmpty()) {
                storageService.deleteFile(thumbnailImgKey);
            }
            thumbnailImgKey = storageService.uploadFile(dto.getThumbnailImg(),
                "carwash_thumbnail",
                carWash.getUuid());
        }

        CarWashProfileUpdateDto updateDto = CarWashProfileUpdateDto.builder()
            .name(dto.getName())
            .address(dto.getAddress())
            .phone(dto.getPhone())
            .businessHours(dto.getBusinessHours())
            .bayCount(dto.getBayCount())
            .businessNumber(dto.getBusinessNumber())
            .thumbnailImgKey(thumbnailImgKey) // 처리된 S3 키
            .build();
        // 변경 사항 저
        carWash.updateCarWash(updateDto);

        return CarWashProfileResponseDto.builder()
            .name(carWash.getName())
            .businessNumber(carWash.getOwnerMember().getBusinessNumber())
            .address(carWash.getAddress())
            .bayCount(carWash.getBayCount())
            .phone(carWash.getPhone())
            .businessHours(carWash.getBusinessHours())
            .thumbnailImgUrl(
                presignedUrlService.generatePresignedUrl(carWash.getThumbnailImgKey(),60)
            )
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
            .build());

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
            throw new EntityNotFoundException("해당하는 세차장이 없습니다.");
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
            .thumbnailImgUrl(presignedUrlService.generatePresignedUrl(carWash.getThumbnailImgKey(), 60))
            .carWashMenus(carWashMenuDtoList)
            .build();
    }

}

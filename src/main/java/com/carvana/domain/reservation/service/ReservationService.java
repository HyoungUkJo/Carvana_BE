package com.carvana.domain.reservation.service;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.customer.member.repository.CustomerMemberRepository;
import com.carvana.domain.reservation.dto.ReservationRequestDto;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.repository.ReservationRepository;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import com.carvana.domain.store.carwash.repository.CarWashMenuRepository;
import com.carvana.domain.store.carwash.repository.CarWashRepository;
import com.carvana.global.notification.service.FcmService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    // MemberRepository필요
    private final CustomerMemberRepository customerMemberRepository;
    // 사진같은 파일을 올릴 FileUploadService도 필요

    private final CarWashRepository carWashRepository;
    private final CarWashMenuRepository carWashMenuRepository;

    // 임시로 FcmTokenService 의존성 주입해서 push 보내기
    private final FcmService fcmService;

    // 예약 생성
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto request) {

        // 요청 엔티티 조회 (회원, 세차장, 메뉴)
        CustomerMember customerMember = customerMemberRepository.findById(request.getCustomerMemberID())
            .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        CarWash carWash = carWashRepository.findById(request.getCarWashId())
            .orElseThrow(() -> new EntityNotFoundException("세차장을 찾을 수 없습니다."));

        CarWashMenu carWashMenu = carWashMenuRepository.findById(request.getMenuId())
            .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

        // 예약 가능 검증
        // 베이정보 및 영업시간 및 예약이 없는지 여부

        // 이미지 처리
        // TODO : 이미지 저장 로직 작성
        String imageUrl = null;

        // 예약 엔티티 생성
        Reservation reservation = Reservation.builder()
            .customerMember(customerMember)
            .carWash(carWash)
            .menu(carWashMenu)
            .reservationDateTime(request.getReservationDateTime())
            .request(request.getRequest())
            .createAt(LocalDateTime.now())
//            .imgUrl(imageUrl)
            .build();

        // 저장
        Reservation savedReservation = reservationRepository.save(reservation);

        // 임시로 push를 예약 서비스에서 호출 -> 추후 이벤트 헨들러로 처리
        fcmService.sendNewReservationNotification(reservation.getCarWash().getOwnerMember().getId(), reservation);


        // 결과
        return ReservationResponseDto.builder()
            .reservationId(savedReservation.getId())
            .reservationDateTime(savedReservation.getReservationDateTime())
            .request(savedReservation.getRequest())
            .imageUrl(savedReservation.getImgUrl())
            .carType(savedReservation.getCarType())
            .status(savedReservation.getStatus())
            .build();

    }
}

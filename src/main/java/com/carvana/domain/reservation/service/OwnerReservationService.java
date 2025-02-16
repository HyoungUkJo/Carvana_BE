package com.carvana.domain.reservation.service;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.customer.member.repository.CustomerMemberRepository;
import com.carvana.domain.reservation.dto.MonthlyStatsDto;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.dto.ReservationUpdateRequestDto;
import com.carvana.domain.reservation.dto.ReservationUpdateResponseDto;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.reservation.repository.ReservationRepository;
import com.carvana.domain.review.repository.ReviewRepository;
import com.carvana.domain.store.carwash.repository.CarWashMenuRepository;
import com.carvana.domain.store.carwash.repository.CarWashRepository;
import com.carvana.global.notification.service.FcmService;
import com.carvana.global.notification.service.NotificationService;
import com.carvana.global.storage.service.StorageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OwnerReservationService {
    private final ReservationRepository reservationRepository;
    // MemberRepository필요
    private final CustomerMemberRepository customerMemberRepository;

    // Review Repository
    private final ReviewRepository reviewRepository;

    // 사진같은 파일을 올릴 FileUploadService도 필요
    private final StorageService storageService;

    private final CarWashRepository carWashRepository;
    private final CarWashMenuRepository carWashMenuRepository;

    // 임시로 FcmTokenService 의존성 주입해서 push 보내기
    private final FcmService fcmService;

    // 예약 notification 의존성 추가
    private final NotificationService notificationService;


    // 처리되지 않은 예약 요청
    public List<ReservationResponseDto> requestedPendingReservation(Long carWashId) {
        List<Reservation> reservationList = reservationRepository.findByCarWashIdAndStatusOrderByCreateAtDesc(carWashId, ReservationStatus.PENDING);


        return reservationList.stream()
            .map(reservation -> ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .request(reservation.getRequest())
//                .imageUrl(reservation.getImgUrl())
                .carType(reservation.getCarType())
                .carNumber(reservation.getCarNumber())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build()).toList();
    }

    // 일정 기간의 매출
    public MonthlyStatsDto getMonthlyStats(Long carWashId) {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        // 1. 예약리스트를가지고온다.
        List<Reservation> completedReservations = reservationRepository.findByCarWashIdAndStatusAndReservationDateTimeBetweenOrderByReservationDateTime(
            carWashId, ReservationStatus.COMPLETED, startOfMonth, endOfMonth
        );

        // 2. 리뷰를 가지고온다.
        int reviewCount = reviewRepository.countByCarWashIdAndCreatedAtBetween(carWashId, startOfMonth, endOfMonth);

        //3. 예약 리스트에서 예약 수와 매출액을 리뷰와 함께 return한다.
        return MonthlyStatsDto.builder()
            .totalReservation(completedReservations.size())
            .totalRevenue(completedReservations.stream()
                .mapToInt(revenue->revenue.calculateTotalPrice())
                .sum())
            .totalReviews(reviewCount)
            .build();
    }

    // 예약 수락 거절
    // Todo : OwnerReservationService로 이동
    @Transactional
    public ReservationUpdateResponseDto updateReservationstatus(Long reservationId, ReservationUpdateRequestDto requestDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("예약을 찾을 수 없습니다."));

        reservation.updateStatus(requestDto);

        // 예약 요청 알림
        notificationService.createReservationNotification(reservation.getCustomerMember(),reservation);


        return ReservationUpdateResponseDto.builder()
            .reservationId(reservationId)
            .status(reservation.getStatus())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    // 한달간의 예약 현황
    // Todo : OwnerReservationService로 이동
    public List<ReservationResponseDto> getMonthlyReservation(Long carWashId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusNanos(1);
        List<ReservationStatus> targetStatus = Arrays.asList(ReservationStatus.PENDING, ReservationStatus.COMPLETED, ReservationStatus.CONFIRMED);
        // List<Reservation> reservations = reservationRepository.findByCarWashIdAndReservationDateTimeBetweenOrderByReservationDateTime(carWashId, start, end);
        List<Reservation> reservations = reservationRepository.findByCarWashIdAndReservationDateTimeBetweenAndStatusIn(carWashId, start, end, targetStatus);
        return reservations.stream()
            .map(reservation -> ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .carType(reservation.getCarType())
                .carNumber(reservation.getCarNumber())
                .request(reservation.getRequest())
//                .imageUrl(reservation.getImgUrl())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build()).collect(Collectors.toList());
    }

    // 예약 전체
    // Todo : OwnerReservationService로 이동
    public List<ReservationResponseDto> getMyReservation(Long customerId) {
        CustomerMember customerMember = customerMemberRepository.findById(customerId).orElseThrow(() -> new EntityNotFoundException("요청한 회원이 없습니다."));

        List<Reservation> reservations = reservationRepository.findByCustomerMemberId(customerId);

        return reservations.stream()
            .map(reservation -> ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .carType(reservation.getCarType())
                .carNumber(reservation.getCarNumber())
                .request(reservation.getRequest())
//                .imageUrl(reservation.getImgUrl())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build())
            .collect(Collectors.toList());
    }

    // 오늘의 예약 현황
    // Todo : OwnerReservationService로 이동
    public List<ReservationResponseDto> getTodayReservation(Long carWashId) {

        LocalDateTime startDateTime = LocalDateTime.now().with(LocalTime.MIN);  // 오늘 00:00:00
        LocalDateTime endDateTime = LocalDateTime.now().with(LocalTime.MAX);    // 오늘 23:59:59.999999999

        List<Reservation> reservations = reservationRepository.findByCarWashIdAndStatusAndReservationDateTimeBetweenOrderByReservationDateTime(carWashId, ReservationStatus.CONFIRMED, startDateTime, endDateTime);

        return reservations.stream()
            .map(reservation -> ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .carType(reservation.getCarType())
                .carNumber(reservation.getCarNumber())
                .request(reservation.getRequest())
//                .imageUrl(reservation.getImgUrl())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build())
            .collect(Collectors.toList());
    }

}

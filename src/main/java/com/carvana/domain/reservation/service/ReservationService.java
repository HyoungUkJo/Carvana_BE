package com.carvana.domain.reservation.service;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.customer.member.repository.CustomerMemberRepository;
import com.carvana.domain.reservation.dto.*;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.reservation.repository.ReservationRepository;
import com.carvana.domain.review.repository.ReviewRepository;
import com.carvana.domain.store.carwash.entity.CarWash;
import com.carvana.domain.store.carwash.entity.CarWashMenu;
import com.carvana.domain.store.carwash.repository.CarWashMenuRepository;
import com.carvana.domain.store.carwash.repository.CarWashRepository;
import com.carvana.global.exception.custom.ReservationException;
import com.carvana.global.notification.service.FcmService;
import com.carvana.global.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    // MemberRepository필요
    private final CustomerMemberRepository customerMemberRepository;

    // Review Repository
    private final ReviewRepository reviewRepository;

    // 사진같은 파일을 올릴 FileUploadService도 필요

    private final CarWashRepository carWashRepository;
    private final CarWashMenuRepository carWashMenuRepository;

    // 임시로 FcmTokenService 의존성 주입해서 push 보내기
    private final FcmService fcmService;

    // 예약 notification 의존성 추가
    private final NotificationService notificationService;

    // 예약 가능한 일자 확인
    public DailyScheduleResponseDto getAvailableReservation(Long carWashId, LocalDate date) {


        // 날짜를 기준으로 디비 쿼리 조회
        List<Reservation> reservations = reservationRepository.findByCarWashIdAndStatusAndReservationDateTimeBetweenOrderByReservationDateTime(
            carWashId,
            ReservationStatus.CONFIRMED,
            date.atStartOfDay(),
            date.atTime(LocalTime.MAX));


        // 베이 리스트 생성
        List<BaySchedule> baySchedules = new ArrayList<>();

        // 세차장 엔티티 조회
        CarWash carWash = carWashRepository.findById(carWashId).orElseThrow(() -> new EntityNotFoundException("해당하는 세차장 정보가 없습니다."));

        // 베이 조회
        for (int i = 1; i <= carWash.getBayCount(); i++) {
            final int bayNumber = i;
            List<Reservation> bayReservations = reservations.stream()
                .filter(reservation -> reservation.getBayNumber() == bayNumber)
                .toList();
            List<TimeSlot> availableSlots = getAvailableTimeSlots(date, bayReservations);

            baySchedules.add(BaySchedule.builder()
                .bayNumber(bayNumber)
                .availableTimeSlot(availableSlots)
                .build());
        }

        return DailyScheduleResponseDto.builder()
            .date(date)
            .baySchedules(baySchedules)
            .build();
    }

    // 베이의 유요한 타임 슬롯을 생성하는 함수
    private List<TimeSlot> getAvailableTimeSlots(LocalDate date, List<Reservation> bayReservations){
        // 새로운 타임슬롯 배열 생성
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalTime openTime = LocalTime.of(9, 0);
        LocalTime closeTime = LocalTime.of(18, 0);
        LocalTime tempTime = openTime;
        while (tempTime.isBefore(closeTime)) {
            // 현재 시간 이전의 데이터는 날린다.
            if (date.equals(LocalDate.now()) && tempTime.isBefore(LocalTime.now())) {
                tempTime = tempTime.plusMinutes(30);
                continue;
            }
            LocalDateTime slotDateTime = date.atTime(tempTime);
            // 예약되어 있는 예약과 시간이 겹치는지 확인이 필요.
            boolean isAvailable = isTimeSlotAvailable(slotDateTime, bayReservations);
            timeSlots.add(TimeSlot.builder().time(tempTime).available(isAvailable).build());
            tempTime = tempTime.plusMinutes(30);

        }
        return timeSlots;
    }

    // 해당 시간에 예약이 있는지 검증하는 함수
    private boolean isTimeSlotAvailable(LocalDateTime slotDateTime, List<Reservation> bayReservations) {
        return bayReservations.stream()
            .noneMatch(reservation -> {
                LocalDateTime reservationStart = reservation.getReservationDateTime();
                LocalDateTime reservationEnd = reservationStart.plusMinutes(reservation.calculateTotalDuration());
                return !slotDateTime.isBefore(reservationStart) && slotDateTime.isBefore(reservationEnd);
            });
    }


    // 두 시간 구간의 겹침 확인
    private boolean isTimeOverlap(
        LocalDateTime start1, LocalDateTime end1,
        LocalDateTime start2, LocalDateTime end2
    ) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    // 예약 생성
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto request) {

        // 요청 엔티티 조회 (회원, 세차장, 메뉴)
        CustomerMember customerMember = customerMemberRepository.findById(request.getCustomerMemberID())
            .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        CarWash carWash = carWashRepository.findById(request.getCarWashId())
            .orElseThrow(() -> new EntityNotFoundException("세차장을 찾을 수 없습니다."));

//        CarWashMenu carWashMenu = carWashMenuRepository.findById(request.getMenuId())
//            .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));

        // 이미지 처리
        // TODO : 이미지 저장 로직 작성
        String imageUrl = null;

        // 예약 엔티티 생성
        Reservation reservation = Reservation.builder()
            .customerMember(customerMember)
            .carWash(carWash)
            .carType(request.getCarType())
            .carNumber(request.getCarNumber())
            .reservationDateTime(request.getReservationDateTime())
            .request(request.getRequest())
            .createAt(LocalDateTime.now())
            .bayNumber(request.getBayNumber())
//            .imgUrl(imageUrl)
            .build();

        // 선택한 메뉴들을 예약에 추가
        for(Long menuId : request.getMenuIds()) {
            CarWashMenu menu = carWashMenuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("메뉴를 찾을 수 없습니다."));
            reservation.addMenu(menu);
        }

        // 베이정보 및 영업시간 및 예약이 없는지 여부

        // 해당 예약의 시작시간부터 끝나는 시간까지의 예약을 조회
        // 그러나 시작 시간 전에 예약이 있고 그 예약이 지금 들어오는 예약시간을 사용하고 있다면 어떻게 처리해야할지..?
        // 일단은 그럼 아예 단순하게 해당 일자의 예약을 전체 조회를 하고 들어온 예약을 비교하는 쿼리를 작성...

        LocalDateTime requestReservationStart = reservation.getReservationDateTime();
        // 세차 완료시간 계산
        LocalDateTime requestReservationEnd = reservation.getReservationDateTime().plusMinutes(reservation.calculateTotalDuration());
        // 예약 요청 일자의 예약을 전체 조회
        LocalDateTime startOfDay = request.getReservationDateTime().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.toLocalDate().atTime(LocalTime.MAX);

        List<Reservation> reservations = reservationRepository.findByCarWashIdAndStatusAndBayNumberAndReservationDateTimeBetweenOrderByReservationDateTime(
            request.getCarWashId(),
            ReservationStatus.CONFIRMED,
            reservation.getBayNumber(),
            startOfDay,
            endOfDay);

        for (Reservation existingReservation : reservations) {
            LocalDateTime existingStart = existingReservation.getReservationDateTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(existingReservation.calculateTotalDuration());

            if (isTimeOverlap(requestReservationStart,requestReservationEnd,existingStart,existingEnd)) {
                throw new ReservationException("해당 시간에 이미 예약이 존재합니다.");
            }

        }

        // 저장
        Reservation savedReservation = reservationRepository.save(reservation);

        // 임시로 push를 예약 서비스에서 호출 -> 추후 이벤트 헨들러로 처리
        fcmService.sendNewReservationNotification(reservation.getCarWash().getOwnerMember().getId(), reservation);

        // 예약 요청 알림
        notificationService.createReservationNotification(customerMember,reservation);

        // 결과
        return ReservationResponseDto.builder()
            .reservationId(savedReservation.getId())
            .reservationDateTime(savedReservation.getReservationDateTime())
            .request(savedReservation.getRequest())
            .imageUrl(savedReservation.getImgUrl())
            .carType(savedReservation.getCarType())
            .carNumber(reservation.getCarNumber())
            .bayNumber(reservation.getBayNumber())
            .status(savedReservation.getStatus())
            .build();
    }

    // 처리되지 않은 예약 요청
    public List<ReservationResponseDto> requestedPendingReservation(Long carWashId) {
        List<Reservation> reservationList = reservationRepository.findByCarWashIdAndStatusOrderByCreateAtDesc(carWashId, ReservationStatus.PENDING);


        return reservationList.stream()
            .map(reservation -> ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .request(reservation.getRequest())
                .imageUrl(reservation.getImgUrl())
                .carType(reservation.getCarType())
                .carNumber(reservation.getCarNumber())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build()).collect(Collectors.toList());
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
                .imageUrl(reservation.getImgUrl())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build()).collect(Collectors.toList());
    }

    // 예약 전체
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
                .imageUrl(reservation.getImgUrl())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build())
            .collect(Collectors.toList());
    }

    // 오늘의 예약 현황
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
                .imageUrl(reservation.getImgUrl())
                .status(reservation.getStatus())
                .bayNumber(reservation.getBayNumber())
                .menuList(reservation.getMenuNameList())
                .build())
            .collect(Collectors.toList());
    }
}

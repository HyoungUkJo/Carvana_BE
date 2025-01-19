package com.carvana.domain.customer.member.service;

import com.carvana.domain.customer.member.dto.CustomerMemberProfileResponseDto;
import com.carvana.domain.customer.member.dto.CustomerMemberProfileUpdateRequestDto;
import com.carvana.domain.customer.member.dto.CustomerMyPageResponseDto;
import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.domain.customer.member.repository.CustomerMemberRepository;
import com.carvana.domain.reservation.dto.ReservationResponseDto;
import com.carvana.domain.reservation.entity.Reservation;
import com.carvana.domain.reservation.entity.ReservationStatus;
import com.carvana.domain.review.dto.ReviewResponseDto;
import com.carvana.domain.review.entity.Review;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerMemberService {
    private final CustomerMemberRepository customerMemberRepository;

    // 프로필 요청 로직
    public CustomerMemberProfileResponseDto getMyProfile(Long customerMemberId) {
        CustomerMember customerMember = customerMemberRepository.findById(customerMemberId).orElseThrow(
            () -> new EntityNotFoundException("해당하는 사용자가 없습니다.")
        );

        return CustomerMemberProfileResponseDto.builder()
            .id(customerMember.getId())
            .name(customerMember.getName())
            .phone(customerMember.getPhone())
            .carType(customerMember.getCarType())
            .carNumber(customerMember.getCarNumber())
            .build();
    }

    // 프로필 수정 로직
    @Transactional
    public CustomerMemberProfileResponseDto updateMyProfile(Long customerMemberId, CustomerMemberProfileUpdateRequestDto updateRequestDto) {
        CustomerMember customerMember = customerMemberRepository.findById(customerMemberId).orElseThrow(
            () -> new EntityNotFoundException("해당하는 사용자가 없습니다.")
        );
        System.out.println("ㅇㅇㅇ"+updateRequestDto.getCarType());
        System.out.println("수정 전: " + customerMember.getCarNumber()); // 로그 추가
        customerMember.updateProfile(updateRequestDto);
        System.out.println("수정 후: " + customerMember.getCarNumber()); // 로그 추가

        return CustomerMemberProfileResponseDto.builder()
            .id(customerMember.getId())
            .name(customerMember.getName())
            .phone(customerMember.getPhone())
            .carType(customerMember.getCarType())
            .carNumber(customerMember.getCarNumber())
            .build();
    }

    // 내 예약 조회
    public List<ReservationResponseDto> getMyReservation(Long customerMemberId) {
        CustomerMember customerMember = customerMemberRepository.findById(customerMemberId).orElseThrow(
            () -> new EntityNotFoundException("해당하는 사용자가 없습니다.")
        );

        return customerMember.getReservationList().stream()
            .map(reservation -> ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .request(reservation.getRequest())
                .imageUrl(reservation.getImgUrl())
                .carType(reservation.getCarType())
                .status(reservation.getStatus())
                .menuList(reservation.getMenuNameList())
                .build()).toList();

    }

    // 나의 페이지
    public CustomerMyPageResponseDto getMyPage(Long customerMemberId){
        CustomerMember customerMember = customerMemberRepository.findById(customerMemberId).orElseThrow(
            () -> new EntityNotFoundException("해당하는 사용자가 없습니다.")
        );

        List<ReservationResponseDto> reservations = customerMember.getReservationList().stream()
            .filter(reservation -> reservation.getStatus() == ReservationStatus.COMPLETED)
            .map(reservation -> ReservationResponseDto.builder()
                .reservationId(reservation.getId())
                .reservationDateTime(reservation.getReservationDateTime())
                .request(reservation.getRequest())
                .imageUrl(reservation.getImgUrl())
                .carType(reservation.getCarType())
                .status(reservation.getStatus())
                .menuList(reservation.getMenuNameList())
                .build()).toList();

        List<ReviewResponseDto> reviews = customerMember.getReviews().stream()
            .map(review -> ReviewResponseDto.builder()
                .customerName(customerMember.getName())
                .rating(review.getRating())
                .content(review.getContent())
                .imageUrls(review.getImageUrls())
                .createdAt(review.getCreatedAt())
                .build()).toList();


        return CustomerMyPageResponseDto.builder()
            .name(customerMember.getName())
            .phone(customerMember.getPhone())
            .carNumber(customerMember.getCarNumber())
            .carType(customerMember.getCarType())
            .reviews(reviews)
            .reservations(reservations)
            .build();

    }

}

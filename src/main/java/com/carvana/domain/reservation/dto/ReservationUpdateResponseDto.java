package com.carvana.domain.reservation.dto;

import com.carvana.domain.reservation.entity.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReservationUpdateResponseDto {
    private Long reservationId;
    private ReservationStatus status;
    private LocalDateTime updatedAt;

    @Builder
    public ReservationUpdateResponseDto(Long reservationId, ReservationStatus status, LocalDateTime updatedAt) {
        this.reservationId = reservationId;
        this.status = status;
        this.updatedAt = updatedAt;
    }
}

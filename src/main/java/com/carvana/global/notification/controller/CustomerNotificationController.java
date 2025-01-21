package com.carvana.global.notification.controller;

import com.carvana.domain.customer.member.entity.CustomerMember;
import com.carvana.global.notification.dto.NotificationResponseDto;
import com.carvana.global.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "손님 알림 목록", description = "알림과 관련된 API")
@RequestMapping("/api/notifications/customer")
public class CustomerNotificationController {
    private final NotificationService notificationService;


    // 알림 목록 조회
    @Operation(summary = "알림 목록", description = "나에게 온 알림 확인하는 api")
    @GetMapping("/{customerId}/list")
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(@PathVariable Long customerId) {
        List<NotificationResponseDto> notifications =
            notificationService.getCustomerNotifications(customerId);
        return ResponseEntity.ok(notifications);
    }
}

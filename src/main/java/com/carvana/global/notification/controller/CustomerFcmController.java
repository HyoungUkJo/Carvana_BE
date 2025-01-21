package com.carvana.global.notification.controller;

import com.carvana.global.notification.dto.FcmTokenSendRequestDto;
import com.carvana.global.notification.service.CustomerFcmService;
import com.carvana.global.notification.service.FcmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 FCM 컨트롤러", description = "사용자 FCM 컨트롤러 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/notification")
public class CustomerFcmController {

    private final CustomerFcmService customerFcmService;
    @PostMapping("/sendToken")
    public ResponseEntity<Void> saveToken(@RequestBody FcmTokenSendRequestDto fcmTokenSendRequestDto){
        Long customerId = fcmTokenSendRequestDto.getId();
        customerFcmService.saveCustomerToken(customerId, fcmTokenSendRequestDto.getToken());

        return ResponseEntity.ok().build();
    }
}

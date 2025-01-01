package com.carvana.global.notification.controller;

import com.carvana.global.notification.dto.FcmTokenSendRequestDto;
import com.carvana.global.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class FcmController {

    private final FcmService fcmService;
    @PostMapping("/sendToken")
    public ResponseEntity<Void> saveToken(@RequestBody FcmTokenSendRequestDto fcmTokenSendRequestDto){
        Long ownerId = fcmTokenSendRequestDto.getId();
        fcmService.saveOwnerToken(ownerId, fcmTokenSendRequestDto.getToken());

        return ResponseEntity.ok().build();
    }
}

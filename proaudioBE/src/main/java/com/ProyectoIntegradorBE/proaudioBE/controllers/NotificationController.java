package com.ProyectoIntegradorBE.proaudioBE.controllers;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.*;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    private NotificationResponseDto createNotification(@RequestBody NotificationRequestDto notificationRequestDto) {

        return notificationService.createNotification(notificationRequestDto);

    }

    @GetMapping("/all")
    private NotificationListResponseDto getNotificationList(@RequestParam(required = false) String type,
                                                            @RequestParam(required = false) Boolean completed,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "10") Integer size,
                                                            @RequestParam(required = false) String direction) {

        return notificationService.findAll(type, completed, name, page, size, direction);

    }

    @PostMapping("/read/{notificationId}")
    private NotificationUserResponseDto readNotification(@PathVariable Long notificationId) {

        return notificationService.readNotification(notificationId);

    }

    @GetMapping("/types")
    private NotificationTypesResponseDto getNotifiacitonTypes() {

        return notificationService.findAllTypes();

    }

    @GetMapping("/details/{id}")
    private NotificationDetailsResponseDto getNotifiacitonDetails(@PathVariable Long id) {

        return notificationService.getNotificationDetails(id);

    }

}

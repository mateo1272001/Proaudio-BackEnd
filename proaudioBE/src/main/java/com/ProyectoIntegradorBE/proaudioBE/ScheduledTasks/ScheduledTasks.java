package com.ProyectoIntegradorBE.proaudioBE.ScheduledTasks;

import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.NotificationService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final ProjectService projectService;

    private final NotificationService notificationService;

    @Async
    @Scheduled(cron = "${spring.cron.project-status}")
    public void updateProjectStatus() {
        System.out.println("STATUS CRON TASK EXECUTING: " + LocalDateTime.now());
        projectService.updateProjectStatusAutomatically();
    }

    @Async
    @Scheduled(cron = "${spring.cron.daily-notifications}")
    public void dailyActiveNotifications() {
        System.out.println("NOTIFICATIONS CRON TASK EXECUTING: " + LocalDateTime.now());
        notificationService.findActiveNotificationsAndSendToUsers();
    }
}

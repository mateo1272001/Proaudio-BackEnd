package com.ProyectoIntegradorBE.proaudioBE.ScheduledTasks;

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

    @Async
    @Scheduled(cron = "${spring.cron.project-status}")
    public void updateProjectStatus() {
        System.out.println("CRON TASK EXECUTING: " + LocalDateTime.now());
        projectService.updateProjectStatusAutomatically();
    }
}

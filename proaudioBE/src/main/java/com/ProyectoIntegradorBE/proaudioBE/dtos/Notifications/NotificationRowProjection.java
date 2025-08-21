package com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications;

public interface NotificationRowProjection {

    Long getNotificationId();

    String getTitle();

    String getDescription();

    Boolean getIsSolved();

    Boolean getIsSeen();

    java.time.LocalDateTime getCreatedAt();

}

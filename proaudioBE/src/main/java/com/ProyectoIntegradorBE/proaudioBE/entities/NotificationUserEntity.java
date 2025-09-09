package com.ProyectoIntegradorBE.proaudioBE.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "notification_user")
public class NotificationUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationUserId;

    private Long userId;

    private Long notificationId;

    private Boolean isRead;
}

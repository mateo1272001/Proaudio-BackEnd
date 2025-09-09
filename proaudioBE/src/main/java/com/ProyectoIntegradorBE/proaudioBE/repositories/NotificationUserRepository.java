package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationUserRepository extends CrudRepository<NotificationUserEntity, Long> {

    Optional<NotificationUserEntity> findByUserIdAndNotificationIdAndIsRead(Long userId, Long notificationId,
                                                                            boolean b);

    Optional<NotificationUserEntity> findByUserIdAndNotificationId(Long userId, Long notificationId);
}

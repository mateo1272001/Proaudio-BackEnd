package com.ProyectoIntegradorBE.proaudioBE.repositories;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.ActiveNotificationsAndUsersDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.NotificationRowProjection;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO notification_user (user_id, notification_id, is_read)
            SELECT u.user_id, :notificationId, 0
            FROM user u
            WHERE u.status = 'ENABLED'
            """, nativeQuery = true)
    void linkAllUsersToNotification(Long notificationId);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO notification_user (user_id, notification_id, is_read)
            SELECT u.user_id, :notificationId, 0
            FROM user u
            WHERE u.status = 'ENABLED' AND u.user_id IN (:userIds)
            """, nativeQuery = true)
    void linkUsersToNotification(Long notificationId, List<Long> userIds);

    List<NotificationEntity> findByTypeAndEntityIdAndActionIdAndIsSolved(NotificationTypeEnum notificationTypeEnum,
                                                                         Long idProject, Long actionId,
                                                                         Boolean isSolved);

    @Query(value = """
            SELECT n.notification_id, n.title, n.description, u.user_id, u.email
            FROM notification n
            INNER JOIN notification_user nu ON (n.notification_id = nu.notification_id)
            INNER JOIN user u ON (u.user_id = nu.user_id)
            WHERE n.is_solved = 0 AND (n.expires_at is null or n.expires_at > now())
            """, nativeQuery = true)
    List<ActiveNotificationsAndUsersDto> findUnsolved();


    @Query(value = """
            SELECT 
                n.notification_id  AS notificationId,
                n.title            AS title,
                n.description      AS description,
                n.is_solved        AS isSolved,
                nu.is_read         AS isSeen,
                n.created_at       AS createdAt
            FROM notification n
            JOIN notification_user nu 
              ON nu.notification_id = n.notification_id
            WHERE nu.user_id = :userId
              AND (:type IS NULL OR n.type = :type)
              AND (
                    :name IS NULL
                 OR  LOWER(n.title)       LIKE LOWER(CONCAT('%', :name, '%'))
                 OR  LOWER(n.description) LIKE LOWER(CONCAT('%', :name, '%'))
              )
              AND (
                    :completed IS NULL
                 OR (:completed = TRUE  AND n.is_solved = TRUE  AND nu.is_read = TRUE)
                 OR (:completed = FALSE AND (n.is_solved = FALSE OR  nu.is_read = FALSE))
              )
            ORDER BY n.notification_id DESC
            """, countQuery = """
            SELECT 
                COUNT(*)
            FROM notification n
            JOIN notification_user nu 
              ON nu.notification_id = n.notification_id
            WHERE nu.user_id = :userId
              AND (:type IS NULL OR n.type = :type)
              AND (
                    :name IS NULL
                 OR  LOWER(n.title)       LIKE LOWER(CONCAT('%', :name, '%'))
                 OR  LOWER(n.description) LIKE LOWER(CONCAT('%', :name, '%'))
              )
              AND (
                    :completed IS NULL
                 OR (:completed = TRUE  AND n.is_solved = TRUE  AND nu.is_read = TRUE)
                 OR (:completed = FALSE AND (n.is_solved = FALSE OR  nu.is_read = FALSE))
              )
            """, nativeQuery = true)
    Page<NotificationRowProjection> searchRowsForUser(@Param("userId") Long userId, @Param("type") String type,
                                                      @Param("completed") Boolean completed, @Param("name") String name,
                                                      Pageable pageable);
}

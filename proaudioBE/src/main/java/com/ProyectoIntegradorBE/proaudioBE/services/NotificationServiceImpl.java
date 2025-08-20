package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Action.ActionResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.*;
import com.ProyectoIntegradorBE.proaudioBE.dtos.Product.PageableDto;
import com.ProyectoIntegradorBE.proaudioBE.dtos.User.UserResponseDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationUserEntity;
import com.ProyectoIntegradorBE.proaudioBE.entities.UserEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.exceptions.BadRequestException;
import com.ProyectoIntegradorBE.proaudioBE.mappers.NotificationMapper;
import com.ProyectoIntegradorBE.proaudioBE.mappers.NotificationUserMapper;
import com.ProyectoIntegradorBE.proaudioBE.repositories.NotificationRepository;
import com.ProyectoIntegradorBE.proaudioBE.repositories.NotificationUserRepository;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.ActionService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.NotificationService;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final ActionService actionService;

    private final EmailService emailService;

    private final UtilService utilService;

    private final UserService userService;

    private final NotificationRepository notificationRepository;

    private final NotificationUserRepository notificationUserRepository;

    private final NotificationMapper notificationMapper;

    private final NotificationUserMapper notificationUserMapper;

    @Override
    public NotificationResponseDto createNotification(NotificationRequestDto notificationRequestDto) {

        Long actionId = Objects.nonNull(notificationRequestDto.getActionKey()) ?
                actionService.getAction(notificationRequestDto.getActionKey()).getActionId() : null;

        NotificationEntity notificationEntity = new NotificationEntity();

        notificationEntity.setTitle(notificationRequestDto.getTitle());

        notificationEntity.setDescription(notificationRequestDto.getDescription());

        notificationEntity.setExpiresAt(
                Objects.nonNull(notificationRequestDto.getExpiresAt()) ? notificationRequestDto.getExpiresAt() : null);

        notificationEntity.setType(notificationRequestDto.getType());

        notificationEntity.setActionId(actionId);

        notificationEntity.setEntityId(notificationRequestDto.getEntityId());

        notificationEntity.setCreatedAt(LocalDateTime.now());

        notificationEntity.setIsSolved(notificationRequestDto.getIsSolved());

        notificationEntity = notificationRepository.save(notificationEntity);

        return notificationMapper.toDto(notificationEntity);
    }

    @Override
    @Transactional
    public void createNotificationAndLinkUsers(NotificationRequestDto notificationRequestDto,
                                               List<UserResponseDto> users) {

        if (users.isEmpty()) {
            throw new InternalException("Listado de usuarios no puede estar vacio en las notificaciones");
        }

        NotificationResponseDto notificationResponseDto = createNotification(notificationRequestDto);

        List<Long> userIds = users.stream().map(UserResponseDto::getUserId).toList();
        notificationRepository.linkUsersToNotification(notificationResponseDto.getNotificationId(), userIds);

        String subject = notificationRequestDto.getTitle();
        String message = notificationRequestDto.getDescription();

        emailService.sendSimpleEmailToMultipleDestinations(users, subject, message);

    }

    @Override
    public List<NotificationEntity> findNotificationByActionAndEntity(NotificationTypeEnum notificationTypeEnum,
                                                                      Long idProject, String action, Boolean isSolved) {

        ActionResponseDto actionResponseDto = actionService.getAction(action);

        return notificationRepository.findByTypeAndEntityIdAndActionIdAndIsSolved(notificationTypeEnum, idProject,
                actionResponseDto.getActionId(), isSolved);
    }

    @Override
    public void update(List<NotificationEntity> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void findActiveNotificationsAndSendToUsers() {

        Map<String, String> userMails = new HashMap<>();

        List<ActiveNotificationsAndUsersDto> list = notificationRepository.findUnsolved();

        for (ActiveNotificationsAndUsersDto userNotif : list) {

            String bodyAddition = """
                    %s: %s
                    """.formatted(userNotif.getTitle(), userNotif.getDescription());


            String body = userMails.get(userNotif.getEmail());

            if (StringUtils.isBlank(body)) {

                userMails.put(userNotif.getEmail(), bodyAddition);

            } else {

                userMails.replace(userNotif.getEmail(), """
                        %s
                        %s""".formatted(body, bodyAddition));

            }

        }

        String title = "Alertas pendientes del día";

        emailService.sendMultipleEmailsToUsers(userMails, title);

    }

    @Override
    public NotificationListResponseDto findAll(String type, Boolean completed, String name, Integer page, Integer size,
                                               String direction) {

        String typeParam = (type == null || type.isBlank()) ? null : type.trim().toUpperCase();

        String nameParam = (name == null || name.isBlank()) ? null : name.trim();

        Pageable pageable = PageRequest.of(page, size);

        UserEntity user = userService.getLoggedUser();

        Page<NotificationRowProjection> rows =
                notificationRepository.searchRowsForUser(user.getUserId(), typeParam, completed, nameParam, pageable);

        List<NotificationRowDto> dtos = rows.getContent().stream().map(p -> {
            NotificationRowDto dto = new NotificationRowDto();
            dto.setNotificationId(p.getNotificationId());
            dto.setTitle(p.getTitle());
            dto.setDescription(p.getDescription());
            dto.setIsSolved(p.getIsSolved());
            dto.setIsSeen(p.getIsSeen());
            dto.setCreatedAt(p.getCreatedAt());
            return dto;
        }).toList();

        PageableDto pagination = utilService.buildPageableDto(rows);

        return new NotificationListResponseDto(dtos, pagination);

    }

    @Override
    public NotificationUserResponseDto readNotification(Long notificationId) {

        UserEntity user = userService.getLoggedUser();

        NotificationUserEntity notificationUserEntity =
                notificationUserRepository.findByUserIdAndNotificationIdAndIsRead(user.getUserId(), notificationId,
                        false).orElseThrow(
                        () -> new BadRequestException("Este usuario no tiene una notificación sin leer acá"));

        notificationUserEntity.setIsRead(true);

        notificationUserEntity = notificationUserRepository.save(notificationUserEntity);

        return notificationUserMapper.toDto(notificationUserEntity);

    }

    @Override
    public NotificationTypesResponseDto findAllTypes() {

        return new NotificationTypesResponseDto(Arrays.stream(NotificationTypeEnum.values()).toList());

    }

    @Override
    public NotificationDetailsResponseDto getNotificationDetails(Long id) {

        NotificationEntity notificationEntity = notificationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("¡No hay una notificación con ese id"));

        UserEntity user = userService.getLoggedUser();

        NotificationUserEntity notificationUserEntity =
                notificationUserRepository.findByUserIdAndNotificationId(user.getUserId(),
                        notificationEntity.getNotificationId()).orElseThrow(
                        () -> new BadRequestException("¡El usuario no tiene vínculo con esta notificación!"));

        ActionResponseDto actionResponseDto = actionService.getAction(notificationEntity.getActionId());

        NotificationDetailsResponseDto notificationDetailsResponseDto = new NotificationDetailsResponseDto();
        notificationDetailsResponseDto.setNotificationId(notificationEntity.getNotificationId());
        notificationDetailsResponseDto.setTitle(notificationEntity.getTitle());
        notificationDetailsResponseDto.setIsSolved(notificationEntity.getIsSolved());
        notificationDetailsResponseDto.setIsSeen(notificationUserEntity.getIsRead());
        notificationDetailsResponseDto.setCreatedAt(notificationEntity.getCreatedAt());
        notificationDetailsResponseDto.setExpiresAt(notificationEntity.getExpiresAt());
        notificationDetailsResponseDto.setType(notificationEntity.getType());
        notificationDetailsResponseDto.setEntityId(notificationEntity.getEntityId());
        notificationDetailsResponseDto.setAction(actionResponseDto);
        //        notificationDetailsResponseDto.setTypeData();

        return notificationDetailsResponseDto;
    }

}

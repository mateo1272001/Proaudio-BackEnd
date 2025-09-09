package com.ProyectoIntegradorBE.proaudioBE.services;

import com.ProyectoIntegradorBE.proaudioBE.dtos.Notifications.TypeDataDto;
import com.ProyectoIntegradorBE.proaudioBE.entities.NotificationEntity;
import com.ProyectoIntegradorBE.proaudioBE.enums.NotificationTypeEnum;
import com.ProyectoIntegradorBE.proaudioBE.services.interfaces.TypeDataProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TypeDataService {

    private final Map<NotificationTypeEnum, TypeDataProvider> providers;

    public TypeDataService(List<TypeDataProvider> providerList) {
        this.providers = providerList.stream().collect(Collectors.toMap(TypeDataProvider::getType, p -> p));
    }

    public List<TypeDataDto> build(NotificationEntity notification) {

        var type = notification.getType();
        var provider = providers.get(type);

        if (provider == null) {
            return List.of();
        }

        return provider.buildTypeData(notification);
    }
}

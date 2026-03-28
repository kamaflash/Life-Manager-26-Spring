package com.pet.businessdomain.notificationservice.mapper;

import com.pet.businessdomain.notificationservice.entities.Notification;
import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mappings({
            @Mapping(target = "priority", source = "priority"),
            @Mapping(target = "resourceType", source = "resourceType"),
            @Mapping(target = "expiresAt", source = "expiresAt"),
            @Mapping(target = "updatedAt", source = "updatedAt")
    })
    NotificationDTO toDTO(Notification notification);

    @Mappings({
            @Mapping(target = "priority", source = "priority"),
            @Mapping(target = "resourceType", source = "resourceType"),
            @Mapping(target = "expiresAt", source = "expiresAt"),
            @Mapping(target = "updatedAt", source = "updatedAt")
            // Si quieres ignorar campos automáticos de BD, descomenta:
            // @Mapping(target = "createdAt", ignore = true),
            // @Mapping(target = "updatedAt", ignore = true),
            // @Mapping(target = "deleted", ignore = true)
    })
    Notification toEntity(NotificationDTO dto);

    List<NotificationDTO> toDTOList(List<Notification> notifications);

    List<Notification> toEntityList(List<NotificationDTO> dtos);
}
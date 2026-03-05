package com.pet.businessdomain.notificationservice.mapper;

import com.pet.businessdomain.notificationservice.dto.NotificationDTO;
import com.pet.businessdomain.notificationservice.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "type", source = "type")
    NotificationDTO toDTO(Notification notification);

    @Mapping(target = "type", source = "type")
    Notification toEntity(NotificationDTO dto);

    List<NotificationDTO> toDTOList(List<Notification> notifications);

    List<Notification> toEntityList(List<NotificationDTO> dtos);
}

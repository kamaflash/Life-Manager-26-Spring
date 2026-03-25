package com.pet.businessdomain.personservice.specifications;

import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NotificationSpecification {

    public static Specification<NotificationDTO> filterUnreadNotifications(
            Long userId,
            NotificationType type,
            EnumAll.NotificationResourceType resourceType
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔹 Obligatorios
            predicates.add(cb.equal(root.get("userId"), userId));
            predicates.add(cb.isFalse(root.get("read")));

            // 🔹 Opcionales
            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (resourceType != null) {
                predicates.add(cb.equal(root.get("resourceType"), resourceType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

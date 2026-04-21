package com.pet.businessdomain.formationservice.specifications;

import com.pet.businessdomain.formationservice.entities.Formation;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public class FormationSpecifications {

    public static Specification<Formation> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<Formation> notInIds(Set<Long> excludedIds) {
        return (root, query, cb) -> {
            if (excludedIds == null || excludedIds.isEmpty()) {
                return cb.conjunction(); // Siempre verdadero
            }
            return cb.not(root.get("id").in(excludedIds));
        };
    }

    public static Specification<Formation> searchByTerm(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + searchTerm.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

    public static Specification<Formation> hasCategory(String category) {
        return (root, query, cb) -> {
            if (category == null || category.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("category")), "%" + category.toLowerCase() + "%");
        };
    }

    public static Specification<Formation> hasLevel(String level) {
        return (root, query, cb) -> {
            if (level == null || level.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("level")), "%" + level.toLowerCase() + "%");
        };
    }

    public static Specification<Formation> durationBetween(Integer minDuration, Integer maxDuration) {
        return (root, query, cb) -> {
            if (minDuration == null && maxDuration == null) {
                return cb.conjunction();
            }
            if (minDuration != null && maxDuration != null) {
                return cb.between(root.get("duration"), minDuration, maxDuration);
            }
            if (minDuration != null) {
                return cb.greaterThanOrEqualTo(root.get("duration"), minDuration);
            }
            return cb.lessThanOrEqualTo(root.get("duration"), maxDuration);
        };
    }

    public static Specification<Formation> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return cb.conjunction();
            }
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<Formation> isCurrentlyAvailable() {
        return (root, query, cb) -> {
            LocalDate today = LocalDate.now();
            return cb.and(
                    cb.or(
                            cb.isNull(root.get("startTime")),
                            cb.lessThanOrEqualTo(root.get("startTime"), today)
                    ),
                    cb.or(
                            cb.isNull(root.get("endTime")),
                            cb.greaterThanOrEqualTo(root.get("endTime"), today)
                    )
            );
        };
    }

    public static Specification<Formation> withWorkingDays() {
        return (root, query, cb) -> {
            // Esto fuerza la carga de workingDays
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("workingDays", JoinType.LEFT);
            }
            return cb.conjunction();
        };
    }
}
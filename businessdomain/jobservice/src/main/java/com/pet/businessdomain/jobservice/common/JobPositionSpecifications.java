package com.pet.businessdomain.jobservice.common;
import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.Set;

public class JobPositionSpecifications {

    public static Specification<JobPositionEntity> byCategory(JobCategory category) {
        return (root, query, cb) -> {
            if (category == null) return cb.conjunction();
            return cb.equal(root.get("category"), category);
        };
    }

    public static Specification<JobPositionEntity> excludeTakenOrApplied(Set<Long> jobIds, Set<Long> appliedIds) {
        return (root, query, cb) -> {
            Predicate notTaken = jobIds.isEmpty() ? cb.conjunction() : cb.not(root.get("id").in(jobIds));
            Predicate notApplied = appliedIds.isEmpty() ? cb.conjunction() : cb.not(root.get("id").in(appliedIds));
            return cb.and(notTaken, notApplied);
        };
    }
    public static Specification<JobPositionEntity> byCategoryOrOther(JobCategory category) {
        return (root, query, cb) -> {
            if (category == null) return cb.conjunction(); // si no hay categoría, no filtra
            return cb.or(
                    cb.equal(root.get("category"), category),
                    cb.equal(root.get("category"), JobCategory.OTHER) // siempre incluye OTHER
            );
        };
    }
    // Puedes añadir más specs como byLocation, byXp si quieres filtrar en DB
}

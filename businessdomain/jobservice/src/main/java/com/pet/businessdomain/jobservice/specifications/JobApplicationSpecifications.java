package com.pet.businessdomain.jobservice.specifications;


import com.pet.businessdomain.jobservice.entities.JobApplicationEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationSpecifications {

    public static Specification<JobApplicationEntity> byCharacterId(Long characterId) {
        return (root, query, cb) -> cb.equal(root.get("characterId"), characterId);
    }

    public static Specification<JobApplicationEntity> bySearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("vacancy").get("position").get("title")), pattern),
                    cb.like(cb.lower(root.get("vacancy").get("position").get("company").get("name")), pattern)
            );
        };
    }

    public static Specification<JobApplicationEntity> byStatus(List<String> statusList) {
        return (root, query, cb) -> {
            if (statusList == null || statusList.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("status").in(statusList);
        };
    }

    public static Specification<JobApplicationEntity> byStage(List<String> stageList) {
        return (root, query, cb) -> {
            if (stageList == null || stageList.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("stage").in(stageList);
        };
    }

    public static Specification<JobApplicationEntity> byMinMatchScore(Integer minMatchScore) {
        return (root, query, cb) -> {
            if (minMatchScore == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("matchScore"), minMatchScore);
        };
    }

    public static Specification<JobApplicationEntity> byMaxMatchScore(Integer maxMatchScore) {
        return (root, query, cb) -> {
            if (maxMatchScore == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("matchScore"), maxMatchScore);
        };
    }

    public static Specification<JobApplicationEntity> byFromDate(LocalDateTime fromDate) {
        return (root, query, cb) -> {
            if (fromDate == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("appliedAt"), fromDate);
        };
    }

    public static Specification<JobApplicationEntity> byToDate(LocalDateTime toDate) {
        return (root, query, cb) -> {
            if (toDate == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("appliedAt"), toDate);
        };
    }
}

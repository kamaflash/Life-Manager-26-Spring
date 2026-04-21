package com.pet.businessdomain.jobservice.specifications;

import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.math.BigDecimal;

public class JobVacancySpecifications {

    public static Specification<JobVacancyEntity> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    public static Specification<JobVacancyEntity> hasAvailableSlots() {
        return (root, query, cb) -> cb.gt(root.get("availableSlots"), 0);
    }

    public static Specification<JobVacancyEntity> keywordSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.conjunction();
            }
            Join<JobVacancyEntity, JobPositionEntity> position = root.join("position", JoinType.LEFT);
            Join<JobPositionEntity, CompanyEntity> company = position.join("company", JoinType.LEFT);
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(position.get("title")), pattern),
                    cb.like(cb.lower(company.get("name")), pattern)
            );
        };
    }

    public static Specification<JobVacancyEntity> positionTitles(String positionTitles) {
        return (root, query, cb) -> {
            if (positionTitles == null || positionTitles.isEmpty()) {
                return cb.conjunction();
            }
            Join<JobVacancyEntity, JobPositionEntity> position = root.join("position", JoinType.LEFT);
            String pattern = "%" + positionTitles.toLowerCase() + "%";
            return cb.like(cb.lower(position.get("title")), pattern);
        };
    }

    public static Specification<JobVacancyEntity> companyNames(String companyNames) {
        return (root, query, cb) -> {
            if (companyNames == null || companyNames.isEmpty()) {
                return cb.conjunction();
            }
            Join<JobVacancyEntity, JobPositionEntity> position = root.join("position", JoinType.LEFT);
            Join<JobPositionEntity, CompanyEntity> company = position.join("company", JoinType.LEFT);
            String pattern = "%" + companyNames.toLowerCase() + "%";
            return cb.like(cb.lower(company.get("name")), pattern);
        };
    }

    public static Specification<JobVacancyEntity> categories(String categories) {
        return (root, query, cb) -> {
            if (categories == null || categories.isEmpty()) {
                return cb.conjunction();
            }
            Join<JobVacancyEntity, JobPositionEntity> position = root.join("position", JoinType.LEFT);
            String pattern = "%" + categories.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(position.get("category")), pattern),
                    cb.equal(position.get("category"), "OTHER")
            );
        };
    }

    public static Specification<JobVacancyEntity> contractTypes(String contractTypes) {
        return (root, query, cb) -> {
            if (contractTypes == null || contractTypes.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + contractTypes.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("contractType")), pattern);
        };
    }

    public static Specification<JobVacancyEntity> workModalities(String workModalities) {
        return (root, query, cb) -> {
            if (workModalities == null || workModalities.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + workModalities.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("workModality")), pattern);
        };
    }

    public static Specification<JobVacancyEntity> weeklyHours(String weeklyHours) {
        return (root, query, cb) -> {
            if (weeklyHours == null || weeklyHours.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + weeklyHours + "%";
            return cb.like(cb.toString(root.get("weeklyHours")), pattern);
        };
    }

    public static Specification<JobVacancyEntity> schedule(String schedule) {
        return (root, query, cb) -> {
            if (schedule == null || schedule.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + schedule.toLowerCase() + "%";
            return cb.like(
                    cb.lower(cb.concat(cb.concat(root.get("startTime"), " - "), root.get("endTime"))),
                    pattern
            );
        };
    }

    public static Specification<JobVacancyEntity> minAvailableSlots(Integer minAvailableSlots) {
        return (root, query, cb) -> {
            if (minAvailableSlots == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("availableSlots"), minAvailableSlots);
        };
    }

    public static Specification<JobVacancyEntity> minSalary(BigDecimal minSalary) {
        return (root, query, cb) -> {
            if (minSalary == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("minSalary"), minSalary);
        };
    }

    public static Specification<JobVacancyEntity> maxSalary(BigDecimal maxSalary) {
        return (root, query, cb) -> {
            if (maxSalary == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("maxSalary"), maxSalary);
        };
    }
}
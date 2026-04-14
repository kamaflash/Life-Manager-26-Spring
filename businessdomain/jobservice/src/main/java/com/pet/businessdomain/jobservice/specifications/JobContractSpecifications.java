package com.pet.businessdomain.jobservice.specifications;

import com.pet.businessdomain.jobservice.entities.JobContractEntity;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class JobContractSpecifications {

    public static Specification<JobContractEntity> byCharacterId(Long characterId) {
        return (root, query, cb) -> cb.equal(root.get("characterId"), characterId);
    }

    public static Specification<JobContractEntity> bySearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return cb.conjunction();
            }
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("positionTitle")), pattern),
                    cb.like(cb.lower(root.get("companyName")), pattern)
            );
        };
    }

    public static Specification<JobContractEntity> byStatus(List<String> statusList) {
        return (root, query, cb) -> {
            if (statusList == null || statusList.isEmpty()) {
                return cb.conjunction();
            }
            return root.get("status").in(statusList);
        };
    }

    public static Specification<JobContractEntity> byMinSalary(BigDecimal minSalary) {
        return (root, query, cb) -> {
            if (minSalary == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("baseSalary"), minSalary);
        };
    }

    public static Specification<JobContractEntity> byMaxSalary(BigDecimal maxSalary) {
        return (root, query, cb) -> {
            if (maxSalary == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("baseSalary"), maxSalary);
        };
    }

    public static Specification<JobContractEntity> byContractType(String contractType) {
        return (root, query, cb) -> {
            if (contractType == null || contractType.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("contractType"), contractType);
        };
    }

    public static Specification<JobContractEntity> byWorkModality(String workModality) {
        return (root, query, cb) -> {
            if (workModality == null || workModality.isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("workModality"), workModality);
        };
    }

    public static Specification<JobContractEntity> byFromDate(LocalDateTime fromDate) {
        return (root, query, cb) -> {
            if (fromDate == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("issuedAt"), fromDate);
        };
    }

    public static Specification<JobContractEntity> byToDate(LocalDateTime toDate) {
        return (root, query, cb) -> {
            if (toDate == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("issuedAt"), toDate);
        };
    }
}
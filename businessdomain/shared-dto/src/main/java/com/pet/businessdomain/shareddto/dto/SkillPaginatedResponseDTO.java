package com.pet.businessdomain.shareddto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillPaginatedResponseDTO {
    private List<SkillStateDto> skills;
    private int currentPage;
    private long totalItems;
    private int totalPages;
    private int pageSize;
}
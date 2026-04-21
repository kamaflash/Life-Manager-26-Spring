package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.TimeAdvanceRequestDTO;
import com.pet.businessdomain.shareddto.dto.TimeAdvanceResponseDTO;

public interface TimeAdvanceService {
    TimeAdvanceResponseDTO advance(TimeAdvanceRequestDTO request);
}

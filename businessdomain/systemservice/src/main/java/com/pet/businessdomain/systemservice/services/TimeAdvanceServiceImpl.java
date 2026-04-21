package com.pet.businessdomain.systemservice.services;


import com.pet.businessdomain.shareddto.dto.TimeAdvanceRequestDTO;
import com.pet.businessdomain.shareddto.dto.TimeAdvanceResponseDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TimeAdvanceServiceImpl implements TimeAdvanceService {

    private final Map<EnumSystems.AdvanceType, AdvanceStrategy> strategies;

    // Constructor injection para el mapa de estrategias
    public TimeAdvanceServiceImpl(List<AdvanceStrategy> strategyList) {
        strategies = new EnumMap<>(EnumSystems.AdvanceType.class);
        for (AdvanceStrategy strategy : strategyList) {
            strategies.put(strategy.getType(), strategy);
        }
    }

    @Override
    public TimeAdvanceResponseDTO advance(TimeAdvanceRequestDTO request) {
        log.info("Avanzando tiempo - Tipo: {} - Personaje: {}",
                request.getAdvanceType(), request.getCharacterId());

        AdvanceStrategy strategy = strategies.get(request.getAdvanceType());
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for type: " + request.getAdvanceType());
        }

        return strategy.execute(request);
    }
}

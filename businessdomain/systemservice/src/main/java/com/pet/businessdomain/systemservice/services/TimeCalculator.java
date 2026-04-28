package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.CharacterInventoryResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Slf4j
@Component
public class TimeCalculator {

    // Constantes de horarios
    public static final LocalTime SCHOOL_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime SCHOOL_END_TIME = LocalTime.of(14, 0);
    public static final int BASE_HOURS_PER_DAY = 6;

    /**
     * Calcula la diferencia en minutos entre dos horas, manejando correctamente el cambio de día.
     *
     * @param start Hora de inicio
     * @param end Hora de fin
     * @return Diferencia en minutos (siempre positiva)
     */
    public long calculateMinutesDifference(LocalTime start, LocalTime end) {
        if (end.isBefore(start)) {
            // Cruza la medianoche: ej: 22:00 → 08:00 = 10 horas = 600 minutos
            long minutesToMidnight = Duration.between(start, LocalTime.MIDNIGHT).toMinutes();
            long minutesFromMidnight = Duration.between(LocalTime.MIDNIGHT, end).toMinutes();
            return minutesToMidnight + minutesFromMidnight;
        }
        // Mismo día: ej: 08:00 → 14:00 = 6 horas = 360 minutos
        return Duration.between(start, end).toMinutes();
    }

    /**
     * Calcula la diferencia en horas entre dos horas
     */
    public long calculateHoursDifference(LocalTime start, LocalTime end) {
        return calculateMinutesDifference(start, end) / 60;
    }

    /**
     * Calcula la diferencia en minutos entre dos fechas completas
     */
    public long calculateMinutesDifference(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }

    /**
     * Calcula la diferencia en horas entre dos fechas completas
     */
    public long calculateHoursDifference(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toHours();
    }

    /**
     * Calcula las horas de descanso desde la hora actual hasta el inicio de clases
     */
    // TimeCalculator.java
    // TimeCalculator.java
    public long calculateRestHours(LocalDateTime currentDateTime) {
        // Crear la fecha/hora de inicio de clases para el día actual
        LocalDateTime todaySchoolStart = currentDateTime
                .withHour(SCHOOL_START_TIME.getHour())
                .withMinute(SCHOOL_START_TIME.getMinute())
                .withSecond(0)
                .withNano(0);

        // Si ya pasó la hora de inicio hoy, usar mañana
        LocalDateTime schoolStart;
        if (currentDateTime.isAfter(todaySchoolStart)) {
            schoolStart = todaySchoolStart.plusDays(1);
            log.info("Ya pasó la hora de inicio hoy ({}), usando mañana: {}", todaySchoolStart, schoolStart);
        } else {
            schoolStart = todaySchoolStart;
        }

        long minutes = Duration.between(currentDateTime, schoolStart).toMinutes();
        long hours = minutes / 60;

        log.info("Horas de descanso hasta inicio de clases: {} horas (desde {} hasta {})",
                hours, currentDateTime, schoolStart);

        return Math.max(0, hours);
    }

    /**
     * Calcula las horas de estudio (jornada escolar base)
     */
    public long getStudyHours() {
        return BASE_HOURS_PER_DAY;
    }

    /**
     * Calcula la hora de salida de casa (considerando vehículo)
     */
    public LocalTime calculateDepartureTime(int travelTimeMinutes) {
        return SCHOOL_START_TIME.minusMinutes(travelTimeMinutes);
    }

    /**
     * Calcula la hora de llegada a casa (considerando vehículo)
     */
    public LocalTime calculateArrivalTime(int travelTimeMinutes) {
        return SCHOOL_END_TIME.plusMinutes(travelTimeMinutes);
    }

    /**
     * Obtiene los minutos de viaje según el vehículo del personaje
     */
    public int getTravelTimeMinutes(CharacterDto character) {
        if (character.getInventory() == null || character.getInventory().isEmpty()) {
            return 60; // Sin vehículo: 60 minutos (transporte público)
        }

        for (CharacterInventoryResponseDTO item : character.getInventory()) {
            Long productId = item.getProduct().getId();

            switch (productId.intValue()) {
                case 6:   // Bicicleta
                    return 30;
                case 7:   // Transporte público
                    return 45;
                case 8:   // Moto
                    return 15;
                case 9:   // Coche
                    return 20;
                case 10:  // Caminando
                    return 60;
                default:
                    return 60;
            }
        }

        return 60;
    }
    /**
     * Obtiene el vehículo del personaje
     */
    public String getTravelVehicle(CharacterDto character) {
        if (character.getInventory() == null || character.getInventory().isEmpty()) {
            return "Caminando"; // Sin vehículo: 60 minutos (transporte público)
        }

        for (CharacterInventoryResponseDTO item : character.getInventory()) {
            Long productId = item.getProductId();

            switch (productId.intValue()) {
                case 6:   // Bicicleta
                    return "bicicleta";
                case 7:   // Transporte público
                    return "transporte público";
                case 8:   // Moto
                    return "moto";
                case 9:   // Coche
                    return "coche";
                case 10:  // Caminando
                    return "caminando";
                default:
                    return "caminando";
            }
        }

        return "Caminando";
    }

    /**
     * Calcula el modificador de tiempo según el vehículo
     */
    public double getTravelModifier(CharacterDto character) {
        if (character.getInventory() == null || character.getInventory().isEmpty()) {
            return 1.0;
        }

        for (CharacterInventoryResponseDTO item : character.getInventory()) {
            Long productId = item.getProductId();

            switch (productId.intValue()) {
                case 6: return 0.40;  // Coche
                case 7: return 0.50;  // Moto
                case 8: return 0.20;  // Bici
                case 9: return 0.15;  // Transporte público
                case 10: return 0.05; // Caminando
                default: return 1.0;
            }
        }

        return 1.0;
    }
}
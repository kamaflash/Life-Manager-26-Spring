package com.pet.businessdomain.systemservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.dto.IRPF.TaxDashboardDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumSystems;
import com.pet.businessdomain.systemservice.entities.SystemEntity;
import com.pet.businessdomain.systemservice.mapper.SystemMapper;
import com.pet.businessdomain.systemservice.repository.SystemRepository;
import com.pet.businessdomain.systemservice.transactions.BusinessTransactions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessAndDelegateStrategy implements AdvanceStrategy {

    private final SystemRepository systemRepository;
    private final SystemMapper systemMapper;
    private final SystemService systemService;
    private final BusinessTransactions businessTransactions;
    private final NormalDayStrategy normalDayStrategy;
    private final WeekendStrategy weekendStrategy;

    @Override
    public TimeAdvanceResponseDTO execute(TimeAdvanceRequestDTO request) {
        log.info("=== INICIANDO PROCESAMIENTO DE DATOS Y DELEGACIÓN ===");
        log.info("Personaje ID: {}", request.getCharacterId());

        // 1. Obtener sistema y personaje
        SystemEntity system = systemRepository.findById(request.getCharacterId())
                .orElseThrow(() -> new RuntimeException("System not found"));
        SystemDto systemDto = systemMapper.toDto(system);
        CharacterDto character = businessTransactions.getPerson(systemDto.getUid());

        LocalDateTime currentDateTime = systemDto.getActualityAt();
        log.info("📅 Fecha/Hora actual: {}", currentDateTime);

        // 2. PROCESAR TODOS LOS DATOS PENDIENTES ANTES DE CUALQUIER OTRA COSA
        List<DayEventDTO> allEvents = new ArrayList<>();

        List<DayEventDTO> pendingEvents = processAllPendingData(character, currentDateTime);
        if (pendingEvents != null && !pendingEvents.isEmpty()) {
            allEvents.addAll(pendingEvents);
            log.info("✅ Se procesaron {} eventos de datos pendientes", pendingEvents.size());
        }

        // 3. Determinar qué tipo de día es (fin de semana o día normal)
        boolean isWeekend = isWeekend(currentDateTime.plusDays(1));

        TimeAdvanceResponseDTO response;

        if (isWeekend) {
            log.info("📅 Es FIN DE SEMANA, delegando a WeekendStrategy");

            // Crear copia del request para no modificar el original
            TimeAdvanceRequestDTO weekendRequest = TimeAdvanceRequestDTO.builder()
                    .characterId(request.getCharacterId())
                    .advanceType(EnumSystems.AdvanceType.WEEKEND)
                    .build();

            response = weekendStrategy.execute(weekendRequest);
        } else {
            log.info("📅 Es DÍA NORMAL, delegando a NormalDayStrategy");

            TimeAdvanceRequestDTO normalRequest = TimeAdvanceRequestDTO.builder()
                    .characterId(request.getCharacterId())
                    .advanceType(EnumSystems.AdvanceType.NORMAL_DAY)
                    .build();

            response = normalDayStrategy.execute(normalRequest);
        }

        // 4. Combinar eventos de la respuesta con los eventos de datos pendientes
        if (response.getEvents() != null && !response.getEvents().isEmpty()) {
            // Insertar eventos pendientes al principio para mejor UX
            allEvents.addAll(response.getEvents());
        } else if (response.getEvents() == null) {
            // Si la respuesta no tiene eventos, solo usamos los pendientes
        }

        // Construir respuesta final con eventos combinados
        return TimeAdvanceResponseDTO.builder()
                .success(response.isSuccess())
                .message(response.getMessage())
                .newActualityAt(response.getNewActualityAt())
                .paRemaining(response.getPaRemaining())
                .energyChange(response.getEnergyChange())
                .stressChange(response.getStressChange())
                .xpEarned(response.getXpEarned())
                .statChanges(response.getStatChanges())
                .events(allEvents)
                .build();
    }

    /**
     * Procesa TODOS los datos pendientes del personaje
     */
    /**
     * Procesa TODOS los datos pendientes del personaje y retorna eventos
     */
    public List<DayEventDTO> processAllPendingData(CharacterDto character, LocalDateTime currentDateTime) {
        List<DayEventDTO> events = new ArrayList<>();

        try {
            log.info("🔄 Procesando TODOS los datos pendientes del personaje: {}", character.getId());

            // 1. Procesar entrevistas pendientes
            Map<String, Object> interviewResult = businessTransactions.processPendingInterviews(character.getId());
            if (interviewResult != null && !interviewResult.isEmpty()) {
                addInterviewEvents(events, interviewResult);

                log.info("📋 Entrevistas procesadas: {}", interviewResult);
            }

            // 2. Procesar avance de aplicaciones
            Map<String, Object> advanceResult = businessTransactions.processedAdvance(character.getId(), 70);
            if (advanceResult != null && !advanceResult.isEmpty()) {
                addAdvanceEvents(events, advanceResult, character.getId());

                log.info("📋 Aplicaciones procesadas: {}", advanceResult);
            }

            // 3. Procesar contratos pendientes
            Map<String, Object> contractResult = businessTransactions.processPendingContracts(character.getId());
            if (contractResult != null && !contractResult.isEmpty()) {
                addContractEvents(events, contractResult);
                log.info("📋 Contratos procesados: {}", contractResult);
            }

            // 4. Procesar becas
            Map<String, Object> scholarshipResult = systemService.revisedData(character);
            if (scholarshipResult != null && !scholarshipResult.isEmpty()) {
                int totalProcessed = getIntOrDefault(scholarshipResult, "totalProcessed", 0);
                if (totalProcessed > 0) {
                    addScholarshipEvents(events, scholarshipResult);
                    log.info("📋 Becas procesadas: {}", scholarshipResult);
                }
            }

            // 5. Procesar IRPF
            Map<String, Object> taxResult = processIRPFIfNeeded(character, currentDateTime);
            if (taxResult != null && !taxResult.isEmpty()) {
                int totalProcessed = getIntOrDefault(taxResult, "totalProcessed", 0);
                if (totalProcessed > 0) {
                    addTaxEvents(events, taxResult);
                    log.info("📋 IRPF procesado: {}", taxResult);
                }
            }

            log.info("💾 Datos pendientes procesados - {} eventos generados", events.size());

        } catch (Exception e) {
            log.error("Error procesando datos pendientes: {}", e.getMessage(), e);
            events.add(DayEventDTO.builder()
                    .type("error")
                    .title("⚠️ Error en procesamiento")
                    .description("Ha ocurrido un error al procesar datos pendientes: " + e.getMessage())
                    .build());
        }

        return events;
    }
    /**
     * Determina si una fecha es fin de semana
     */
    private boolean isWeekend(LocalDateTime dateTime) {
        if (dateTime == null) return false;
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * Añade eventos de entrevistas
     */
    private void addInterviewEvents(List<DayEventDTO> events, Map<String, Object> interviewResult) {
        int totalProcessed = getIntOrDefault(interviewResult, "totalProcessed", 0);
        int passed = getIntOrDefault(interviewResult, "passed", 0);
        int failed = getIntOrDefault(interviewResult, "failed", 0);

        if (totalProcessed > 0) {

            if (passed > 0) {
                events.add(DayEventDTO.builder()
                        .type("interview_passed")
                        .title("🎉 ¡Entrevista superada!")
                        .description(String.format("Has superado %d entrevista(s). ¡Pronto recibirás las ofertas!", passed))
                        .build());


            }

            if (failed > 0) {
                events.add(DayEventDTO.builder()
                        .type("interview_failed")
                        .title("😔 Entrevista no superada")
                        .description(String.format("No has superado %d entrevista(s). Sigue preparándote.", failed))
                        .build());
            }
        }
    }
    private Long getLongFromMap(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) return null;
        Object value = map.get(key);
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    private EventResponseDto createInterviewPassedEvent(String companyName, String positionTitle, LocalDateTime interviewDate) {
        EventResponseDto dto = new EventResponseDto();
        dto.setCode("QUARTERLY_REVIEW");
        dto.setTitle("¡Entrevista programada!");
        dto.setDescription(String.format("Has programado la entrevista en %s para el puesto de %s", companyName, positionTitle));
        dto.setType("JOB_OFFER");
        dto.setScope("PERSONAL");
        dto.setStartDate(interviewDate);
        dto.setEndDate(interviewDate.plusHours(1));
        dto.setStatus("IN_PROGRESS");
        dto.setAutoTrigger(true);
        return dto;
    }

    private EventResponseDto createInterviewFailedEvent(String companyName, String positionTitle, String reason) {
        EventResponseDto dto = new EventResponseDto();
        dto.setCode("INTERVIEW_FAILED");
        dto.setTitle("😔 Entrevista no superada");
        dto.setDescription(String.format("No has superado la entrevista en %s para el puesto de %s", companyName, positionTitle));
        dto.setType("interview_failed");
        dto.setScope("character");
        dto.setStartDate(LocalDateTime.now());
        dto.setEndDate(LocalDateTime.now().plusHours(1));
        dto.setStatus("COMPLETED");
        dto.setAutoTrigger(true);
        if (reason != null) {
            dto.setLore(reason);
        }
        return dto;
    }
    /**
     * Añade eventos de avance de aplicaciones
     */
    private void addAdvanceEvents(List<DayEventDTO> events, Map<String, Object> advanceResult, Long characterId) {
        int totalProcessed = getIntOrDefault(advanceResult, "totalProcessed", 0);
        int accepted = getIntOrDefault(advanceResult, "accepted", 0);
        int rejected = getIntOrDefault(advanceResult, "rejected", 0);
// Obtener la lista de applications
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> applications = (List<Map<String, Object>>) advanceResult.getOrDefault("applications", new ArrayList<>());

        if (totalProcessed > 0) {
            if (accepted > 0) {
                events.add(DayEventDTO.builder()
                        .type("application_accepted")
                        .title("📝 ¡Postulación avanzada!")
                        .description(String.format("%d de tus postulaciones han pasado a la siguiente fase.", accepted))
                        .build());
                // Crear evento para cada aplicación que pasó la entrevista
                for (Map<String, Object> application : applications) {
                    // Verificar si esta aplicación fue aprobada
                    String status = (String) application.get("status");
                    if ("INTERVIEW_SCHEDULED".equals(status)) {
                        // Extraer datos de la aplicación
                        Long applicationId = getLongFromMap(application, "id");
                        String companyName = (String) application.get("companyName");
                        String positionTitle = (String) application.get("positionTitle");
                        String interviewNotes = (String) application.get("interviewNotes");

                        // Obtener fecha de entrevista del resultado principal
                        LocalDateTime interviewDate = null;
                        Object interviewDateObj = advanceResult.get("interviewDate");
                        if (interviewDateObj instanceof LocalDate) {
                            interviewDate = ((LocalDate) interviewDateObj).atStartOfDay();
                        } else if (interviewDateObj instanceof String) {
                            try {
                                interviewDate = LocalDateTime.parse((String) interviewDateObj);
                            } catch (Exception e) {
                                interviewDate = LocalDateTime.now();
                            }
                        } else {
                            interviewDate = LocalDateTime.now();
                        }

                        // Crear el evento
                        EventResponseDto eventDto = createInterviewPassedEvent(companyName, positionTitle, interviewDate);

                        try {
                            businessTransactions.createEvent(characterId, eventDto);
                            log.info("✅ Evento creado para entrevista pasada en: {} - {}", companyName, positionTitle);
                        } catch (Exception e) {
                            log.error("Error creando evento para entrevista: {}", e.getMessage());
                        }
                    }
                }
            }

            if (rejected > 0) {
                events.add(DayEventDTO.builder()
                        .type("application_rejected")
                        .title("📝 Postulación no seleccionada")
                        .description(String.format("%d de tus postulaciones no cumplen los requisitos.", rejected))
                        .build());
            }
        }
    }

    /**
     * Añade eventos de contratos
     */
    private void addContractEvents(List<DayEventDTO> events, Map<String, Object> contractResult) {
        int totalProcessed = getIntOrDefault(contractResult, "totalProcessed", 0);
        int contractsGenerated = getIntOrDefault(contractResult, "contractsGenerated", 0);

        if (contractsGenerated > 0) {
            events.add(DayEventDTO.builder()
                    .type("contract_generated")
                    .title("📄 ¡Nuevo contrato recibido!")
                    .description(String.format("Se ha generado %d contrato(s). ¡Revisa tus ofertas!", contractsGenerated))
                    .build());
        }

        if (totalProcessed > 0 && contractsGenerated == 0) {
            int alreadyHaveContract = getIntOrDefault(contractResult, "alreadyHaveContract", 0);
            if (alreadyHaveContract > 0) {
                events.add(DayEventDTO.builder()
                        .type("contract_info")
                        .title("ℹ️ Contratos actualizados")
                        .description("Tus contratos ya están actualizados.")
                        .build());
            }
        }
    }

    /**
     * Añade eventos de notificaciones
     */
    private void addNotificationEvents(List<DayEventDTO> events, Map<String, Object> notificationResult) {
        int totalProcessed = getIntOrDefault(notificationResult, "totalProcessed", 0);
        int notificationsSent = getIntOrDefault(notificationResult, "notificationsSent", 0);

        if (notificationsSent > 0) {
            events.add(DayEventDTO.builder()
                    .type("notifications")
                    .title("📬 Nuevas notificaciones")
                    .description(String.format("Tienes %d notificaciones nuevas pendientes de revisar.", notificationsSent))
                    .build());
        }
    }

    /**
     * Añade eventos de facturas
     */
    private void addInvoiceEvents(List<DayEventDTO> events, Map<String, Object> invoiceResult) {
        int totalProcessed = getIntOrDefault(invoiceResult, "totalProcessed", 0);
        int invoicesGenerated = getIntOrDefault(invoiceResult, "invoicesGenerated", 0);
        int invoicesPaid = getIntOrDefault(invoiceResult, "invoicesPaid", 0);

        if (invoicesGenerated > 0) {
            events.add(DayEventDTO.builder()
                    .type("invoices_generated")
                    .title("📑 Facturas generadas")
                    .description(String.format("Se han generado %d facturas nuevas.", invoicesGenerated))
                    .build());
        }

        if (invoicesPaid > 0) {
            events.add(DayEventDTO.builder()
                    .type("invoices_paid")
                    .title("💰 Facturas cobradas")
                    .description(String.format("Se han cobrado %d facturas pendientes.", invoicesPaid))
                    .build());
        }
    }

    /**
     * Helper para obtener int de Map
     */
    private int getIntOrDefault(Map<String, Object> map, String key, int defaultValue) {
        if (map == null || !map.containsKey(key)) return defaultValue;
        Object value = map.get(key);
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Añade eventos basados en resultados de becas
     */
    private void addScholarshipEvents(List<DayEventDTO> events, Map<String, Object> scholarshipResult) {
        int totalProcessed = getIntOrDefault(scholarshipResult, "totalProcessed", 0);
        int approved = getIntOrDefault(scholarshipResult, "approved", 0);
        int rejected = getIntOrDefault(scholarshipResult, "rejected", 0);
        @SuppressWarnings("unchecked")
        List<String> details = (List<String>) scholarshipResult.getOrDefault("details", new ArrayList<>());

        if (totalProcessed > 0) {
            if (approved > 0) {
                events.add(DayEventDTO.builder()
                        .type("scholarship_approved")
                        .title("🎓 ¡Beca aprobada!")
                        .description(String.format("Has obtenido %d beca(s). ¡Revisa los detalles en tu panel!", approved))
                        .build());
            }

            if (rejected > 0) {
                events.add(DayEventDTO.builder()
                        .type("scholarship_rejected")
                        .title("📚 Beca no concedida")
                        .description(String.format("%d de tus solicitudes de beca no han sido aprobadas. Sigue mejorando tu perfil.", rejected))
                        .build());
            }

            // Evento con resumen detallado
            if (!details.isEmpty()) {
                String summary = String.join(", ", details);
                if (summary.length() > 150) {
                    summary = summary.substring(0, 147) + "...";
                }
                events.add(DayEventDTO.builder()
                        .type("scholarship_summary")
                        .title("📊 Resumen de becas")
                        .description(summary)
                        .build());
            }
        }
    }
    /**
     * Procesa la declaración de IRPF si corresponde y retorna un mapa con el resultado
     */
    public Map<String, Object> processIRPFIfNeeded(CharacterDto character, LocalDateTime currentDateTime) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> taxEvents = new ArrayList<>();
        int processedCount = 0;
        boolean paymentProcessed = false;
        boolean refundProcessed = false;
        boolean neutralProcessed = false;

        try {
            int currentYear = currentDateTime.getYear();
            int previousYear = currentYear - 1;

            log.info("💰 Verificando declaración IRPF para año: {}", previousYear);

            Boolean isTaxPeriodActive = businessTransactions.isTaxPeriodActive();
            if (isTaxPeriodActive == null || !isTaxPeriodActive) {
                log.info("No hay período fiscal activo para presentar declaración");
                result.put("totalProcessed", 0);
                result.put("hasTaxPeriod", false);
                result.put("details", new ArrayList<>());
                return result;
            }

            TaxDashboardDTO dashboard = businessTransactions.getTaxDashboard(character.getId(), previousYear);
            if (dashboard == null) {
                log.info("No se pudo obtener información fiscal para el personaje");
                result.put("totalProcessed", 0);
                result.put("hasDashboard", false);
                result.put("details", new ArrayList<>());
                return result;
            }

            if (dashboard.getEstimatedResult() != null) {
                log.info("El personaje ya tiene información fiscal para el año {}", previousYear);
                result.put("totalProcessed", 0);
                result.put("alreadyFiled", true);
                result.put("details", List.of("Ya has presentado la declaración del año " + previousYear));
                return result;
            }

            Map<String, Object> filing = businessTransactions.createOrUpdateTaxFilingDraft(character.getId(), previousYear);
            if (filing == null) {
                log.warn("No se pudo obtener/crear borrador de declaración");
                result.put("totalProcessed", 0);
                result.put("error", "No se pudo crear el borrador");
                result.put("details", new ArrayList<>());
                return result;
            }

            Long filingId = extractLongFromMap(filing, "id");
            String resultType = extractStringFromMap(filing, "resultType");
            BigDecimal amount = extractBigDecimalFromMap(filing, "amountToPay");

            if ("TO_RECEIVE".equals(resultType)) {
                amount = extractBigDecimalFromMap(filing, "amountToReceive");
            }

            if (filingId == null) {
                log.warn("No se pudo obtener filingId del borrador");
                result.put("totalProcessed", 0);
                result.put("error", "No se pudo obtener ID del borrador");
                result.put("details", new ArrayList<>());
                return result;
            }

            if (character.getAccounts() != null && !character.getAccounts().isEmpty()) {
                Long accountId = character.getAccounts().get(0).getId();

                Map<String, Object> submittedFiling = businessTransactions.submitTaxFiling(filingId, character.getId());

                if (submittedFiling != null) {
                    Map<String, Object> taxEvent = new HashMap<>();

                    if ("TO_PAY".equals(resultType) && amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                        Map<String, Object> paymentResult = businessTransactions.processTaxPayment(filingId, accountId, true);
                        if (paymentResult != null) {
                            SFinanceAccountResponseDto primaryAccount = character.getAccounts().get(0);
                            primaryAccount.setBalance(primaryAccount.getBalance().subtract(amount));
                            paymentProcessed = true;
                            processedCount++;

                            taxEvent.put("type", "tax_payment");
                            taxEvent.put("title", "💰 Declaración de la Renta - Pago Realizado");
                            taxEvent.put("description", String.format("Has pagado %,.2f € de IRPF del año %d.", amount, previousYear));
                            taxEvent.put("amount", amount);
                            taxEvent.put("year", previousYear);
                            taxEvent.put("resultType", "TO_PAY");
                            taxEvents.add(taxEvent);

                            log.info("✅ IRPF: Pago de {}€ procesado", amount);
                        }
                    } else if ("TO_RECEIVE".equals(resultType) && amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                        Map<String, Object> refundResult = businessTransactions.processTaxRefund(filingId, accountId);
                        if (refundResult != null) {
                            SFinanceAccountResponseDto primaryAccount = character.getAccounts().get(0);
                            primaryAccount.setBalance(primaryAccount.getBalance().add(amount));
                            refundProcessed = true;
                            processedCount++;

                            taxEvent.put("type", "tax_refund");
                            taxEvent.put("title", "🎉 Declaración de la Renta - Devolución Recibida");
                            taxEvent.put("description", String.format("Has recibido %,.2f € de devolución del IRPF del año %d.", amount, previousYear));
                            taxEvent.put("amount", amount);
                            taxEvent.put("year", previousYear);
                            taxEvent.put("resultType", "TO_RECEIVE");
                            taxEvents.add(taxEvent);

                            log.info("✅ IRPF: Devolución de {}€ recibida", amount);
                        }
                    } else if ("NEUTRAL".equals(resultType)) {
                        neutralProcessed = true;
                        processedCount++;

                        taxEvent.put("type", "tax_neutral");
                        taxEvent.put("title", "📄 Declaración de la Renta - Regularizada");
                        taxEvent.put("description", String.format("Tu declaración del año %d ha sido regularizada sin deuda ni devolución.", previousYear));
                        taxEvent.put("amount", BigDecimal.ZERO);
                        taxEvent.put("year", previousYear);
                        taxEvent.put("resultType", "NEUTRAL");
                        taxEvents.add(taxEvent);
                    }

                    if (processedCount > 0) {
                        businessTransactions.updatePerson(character);
                    }
                }
            } else {
                // Sin cuenta bancaria configurada
                if ("TO_PAY".equals(resultType) && amount != null) {
                    Map<String, Object> warningEvent = new HashMap<>();
                    warningEvent.put("type", "tax_warning");
                    warningEvent.put("title", "⚠️ Declaración de la Renta Pendiente");
                    warningEvent.put("description", String.format("Tienes un pago pendiente de %,.2f € de la renta del año %d. Configura una cuenta bancaria.", amount, previousYear));
                    warningEvent.put("amount", amount);
                    warningEvent.put("year", previousYear);
                    taxEvents.add(warningEvent);
                } else if ("TO_RECEIVE".equals(resultType) && amount != null) {
                    Map<String, Object> warningEvent = new HashMap<>();
                    warningEvent.put("type", "tax_warning");
                    warningEvent.put("title", "💰 Devolución de la Renta Disponible");
                    warningEvent.put("description", String.format("Tienes %,.2f € por devolver de la renta del año %d. Configura una cuenta bancaria.", amount, previousYear));
                    warningEvent.put("amount", amount);
                    warningEvent.put("year", previousYear);
                    taxEvents.add(warningEvent);
                }
            }

            result.put("totalProcessed", processedCount);
            result.put("paymentProcessed", paymentProcessed);
            result.put("refundProcessed", refundProcessed);
            result.put("neutralProcessed", neutralProcessed);
            result.put("hasAccount", character.getAccounts() != null && !character.getAccounts().isEmpty());
            result.put("taxEvents", taxEvents);
            result.put("details", taxEvents.stream()
                    .map(e -> e.get("description"))
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            log.error("Error procesando IRPF: {}", e.getMessage(), e);
            result.put("totalProcessed", 0);
            result.put("error", e.getMessage());
            result.put("details", List.of("Error al procesar IRPF: " + e.getMessage()));
        }

        return result;
    }

    // Métodos auxiliares
    private Long extractLongFromMap(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) return null;
        Object value = map.get(key);
        if (value instanceof Number) return ((Number) value).longValue();
        return null;
    }

    private String extractStringFromMap(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) return null;
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private BigDecimal extractBigDecimalFromMap(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) return null;
        Object value = map.get(key);
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return null;
    }

    /**
     * Añade eventos basados en resultados de IRPF
     */
    public void addTaxEvents(List<DayEventDTO> events, Map<String, Object> taxResult) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> taxEvents = (List<Map<String, Object>>) taxResult.getOrDefault("taxEvents", new ArrayList<>());

        int paymentProcessed = getIntOrDefault(taxResult, "paymentProcessed", 0);
        int refundProcessed = getIntOrDefault(taxResult, "refundProcessed", 0);
        int neutralProcessed = getIntOrDefault(taxResult, "neutralProcessed", 0);

        if (paymentProcessed > 0 || refundProcessed > 0 || neutralProcessed > 0) {
            for (Map<String, Object> taxEvent : taxEvents) {
                String type = (String) taxEvent.get("type");
                String title = (String) taxEvent.get("title");
                String description = (String) taxEvent.get("description");
                BigDecimal amount = (BigDecimal) taxEvent.get("amount");

                DayEventDTO.DayEventDTOBuilder builder = DayEventDTO.builder()
                        .type(type)
                        .title(title)
                        .description(description);

                if (amount != null && ("tax_payment".equals(type) || "tax_warning".equals(type))) {
                    builder.moneyEarned(amount.negate().intValue());
                } else if (amount != null && "tax_refund".equals(type)) {
                    builder.moneyEarned(amount.intValue());
                }

                events.add(builder.build());
            }
        }
    }

    @Override
    public EnumSystems.AdvanceType getType() {
        return EnumSystems.AdvanceType.PROCESS_AND_DELEGATE;
    }
}
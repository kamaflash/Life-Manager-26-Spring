package com.pet.businessdomain.systemservice.transactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.dto.IRPF.TaxDashboardDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.EnumFormation;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.pet.businessdomain.systemservice.repository.SystemRepository;

@Slf4j
@Service
public class BusinessTransactions {
    @Autowired
    private SystemRepository systemRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;
    /*private final WebClient.Builder webClientBuilder;

    public CustomerRestController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }*/

    //webClient requires HttpClient library to work propertly
    HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(5))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    public CharacterDto getPerson(Long uid) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-PERSONSERVICE/api/characters")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/uid/full/{uid}", uid)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from User service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(CharacterDto.class)
                    .block(); // devuelve UserDto directamente

        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
            return null; // o lanza excepción, según tu diseño
        }
    }
    public CharacterDto updatePerson(CharacterDto character) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-PERSONSERVICE/api/characters")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.put()
                    .uri("/stats/{id}", character.getId()) // ajusta endpoint si es distinto
                    .bodyValue(character)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from User service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(CharacterDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return null;
        }
    }
    public FormationDto getFormation(Long formationId) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/formations")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/{id}", formationId)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from User service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(FormationDto.class)
                    .block(); // devuelve UserDto directamente

        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
            return null; // o lanza excepción, según tu diseño
        }
    }
    public CharacterTrainingDto getTrainning(Long id, Long trainingId) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/trainer")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/dto/{id}/{trainingId}", id, trainingId)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from User service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(CharacterTrainingDto.class)
                    .block(); // devuelve UserDto directamente

        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
            return null; // o lanza excepción, según tu diseño
        }
    }
    public CharacterTrainingDto updateAppTrainning(CharacterTrainingDto character) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/trainer")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.put()
                    .uri("/dto/{id}", character.getId()) // ajusta endpoint si es distinto
                    .bodyValue(character)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from User service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(CharacterTrainingDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return null;
        }
    }
    public List<ScholarshipApplicationDto> getBecas(Long characterId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/scholarships")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/by-character/list/{characterId}")
                        .build(characterId))
                .retrieve()
                .bodyToFlux(ScholarshipApplicationDto.class)
                .collectList()
                .block();
    }
    public ScholarshipDto getScholarshipById(Long scholarshipId) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/scholarships")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/dto/{id}", scholarshipId) // ajusta endpoint si es distinto
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from User service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(ScholarshipDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza el estado de una solicitud de beca
     */
    public ScholarshipApplicationDto updateScholarshipStatus(Long applicationId, EnumFormation.ApplicationStatus status) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/scholarships")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.put()
                    .uri("/dto/{applicationId}/statuss?statuss={status}", applicationId, status.name())  // ✅ Enviar como query param
                    .retrieve()
                    .onStatus(
                            statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from Formation service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(ScholarshipApplicationDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error updating scholarship status: " + e.getMessage());
            return null;
        }
    }public List<SFinanceAccountResponseDto> getAccount(Long ownerId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FINANCESERVICE/api/accounts")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/owner/full/{ownerId}")
                        .build(ownerId))
                .retrieve()
                .bodyToFlux(SFinanceAccountResponseDto.class)
                .collectList()
                .block();
    }
    public SIncomeResponseDto setIncome(SIncomeResponseDto dto, Long accountId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FINANCESERVICE/api/incomes")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/{accountId}", accountId)
                .bodyValue(dto)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "FinanceService Error: " + response.statusCode() + " - " + body
                                )))
                )
                .bodyToMono(SIncomeResponseDto.class)
                .block();
    }

    public NotificationDTO setNotifications(NotificationDTO dto) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-NOTIFICATIONSERVICE/api/notifications")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/post")
                .bodyValue(dto) // enviamos el DTO en el body
                .retrieve()
                .bodyToMono(NotificationDTO.class) // esperamos un solo DTO
                .block(); // bloqueamos hasta recibir respuesta
    }
    public Map<String, Object> processedAdvance(Long characterId, Integer minMatchScore) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/job-applications")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Integer score = minMatchScore != null ? minMatchScore : 70;

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/character/{characterId}/process")
                        .queryParam("minMatchScore", score)
                        .build(characterId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
    public Map<String, Object> processPendingInterviews(Long characterId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/job-applications")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/character/{characterId}/process-interviews")
                        .build(characterId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
    /**
     * Procesa las entrevistas aprobadas que tienen al menos 2 días de antigüedad
     * y genera los contratos correspondientes
     */
    public Map<String, Object> processPendingContracts(Long characterId) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/job-applications")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/character/{characterId}/process-contracts")
                            .queryParam("daysToWait", 2)
                            .build(characterId))
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error processing pending contracts for characterId={}: {}", characterId, e.getMessage());
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("error", e.getMessage());
            return errorResult;
        }
    }
    public JobContractDTO generateContract(Long applicationId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/job-applications")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/{applicationId}/generate-contract")
                        .build(applicationId))
                .retrieve()
                .bodyToMono(JobContractDTO.class)
                .block();
    }

    public List<JobApplicationDTO> getApplicationsByCharacterAndStatus(Long characterId, EnumAll.ApplicationStatus status) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/job-applications")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/character/{characterId}/status/{status}")
                        .build(characterId, status))
                .retrieve()
                .bodyToFlux(JobApplicationDTO.class)
                .collectList()
                .block();
    }
    public PayrollDTO createPayroll(PayrollDTO payrollDTO) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/payrolls")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            PayrollDTO response = webClient.post()
                    .uri("/create")
                    .bodyValue(payrollDTO)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            clientResponse  -> clientResponse .bodyToMono(String.class)
                                    .flatMap(body -> {
                                        log.error("Error response from payroll service: {} - {}", clientResponse .statusCode(), body);
                                        return Mono.error(new RuntimeException(
                                                "Error from Systems service: " + clientResponse .statusCode() + " - " + body
                                        ));
                                    })
                    )
                    .bodyToMono(PayrollDTO.class)
                    .block();

            if (response == null) {
                log.error("Received null response from payroll service for characterId={}, jobId={}",
                        payrollDTO.getCharacterId(), payrollDTO.getJobId());
            } else {
                log.info("Payroll created successfully with ID: {}", response.getId());
            }

            return response;

        } catch (Exception e) {
            log.error("Error calling BUSINESSDOMAIN-JOBSERVICE /api/payrolls/create for payroll characterId={} jobId={}: {}",
                    payrollDTO != null ? payrollDTO.getCharacterId() : null,
                    payrollDTO != null ? payrollDTO.getJobId() : null,
                    e.getMessage(), e);
            // NO relanzar la excepción, retornar null explícitamente
            return null;
        }
    }
    /**
     * Marca una nómina como pagada
     */
    public PayrollDTO markPayrollAsPaid(Long payrollId, Long accountId) {
        try {

            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/payrolls")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.post()
                    .uri("/{id}/pay?accountId={accountId}", payrollId, accountId)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from Systems service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(PayrollDTO.class)
                    .block();

        } catch (Exception e) {
            return null;
        }
    }

    // Añade estos métodos a la clase BusinessTransactions

    /**
     * Obtiene el dashboard fiscal de un personaje para un año específico
     */
    public TaxDashboardDTO getTaxDashboard(Long characterId, Integer year) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/dashboard")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/character/{characterId}/year/{year}", characterId, year)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(TaxDashboardDTO.class)
                    .block();

        } catch (Exception e) {
            log.error("Error getting tax dashboard for characterId={}, year={}: {}", characterId, year, e.getMessage());
            return null;
        }
    }

    /**
     * Verifica si hay un período fiscal activo para presentar declaración
     * Retorna un Boolean simple
     */
    public Boolean isTaxPeriodActive() {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/periods")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/can-file")
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(Boolean.class)
                    .block();

        } catch (Exception e) {
            log.error("Error checking if tax period is active: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene o crea el borrador de la declaración de un personaje
     * Retorna Map porque no existe DTO específico para TaxFiling
     */
    public Map<String, Object> createOrUpdateTaxFilingDraft(Long characterId, Integer taxYear) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/filings")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.post()
                    .uri("/draft/character/{characterId}/year/{taxYear}", characterId, taxYear)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error creating/updating tax filing draft for characterId={}, year={}: {}", characterId, taxYear, e.getMessage());
            return null;
        }
    }

    /**
     * Presenta la declaración de un personaje
     * Retorna Map porque no existe DTO específico para TaxFiling
     */
    public Map<String, Object> submitTaxFiling(Long filingId, Long characterId) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/filings")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.post()
                    .uri("/{filingId}/submit?characterId={characterId}", filingId, characterId)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error submitting tax filing for filingId={}, characterId={}: {}", filingId, characterId, e.getMessage());
            return null;
        }
    }

    /**
     * Procesa el pago de una declaración (cuando el resultado es a pagar)
     */
    public Map<String, Object> processTaxPayment(Long filingId, Long accountId, boolean fullPayment) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/filings")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.post()
                    .uri("/{filingId}/payment?accountId={accountId}&fullPayment={fullPayment}", filingId, accountId, fullPayment)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error processing tax payment for filingId={}, accountId={}: {}", filingId, accountId, e.getMessage());
            return null;
        }
    }

    /**
     * Procesa la devolución de una declaración (cuando el resultado es a devolver)
     */
    public Map<String, Object> processTaxRefund(Long filingId, Long accountId) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/filings")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.post()
                    .uri("/{filingId}/refund?accountId={accountId}", filingId, accountId)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error processing tax refund for filingId={}, accountId={}: {}", filingId, accountId, e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene el período fiscal activo
     * Retorna Map porque TaxFilingPeriodDTO no existe
     */
    public Map<String, Object> getActiveTaxPeriod() {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/periods")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/active")
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error getting active tax period: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene la declaración de un personaje por año
     * Retorna Map porque no existe DTO específico para TaxFiling
     */
    public Map<String, Object> getTaxFilingByCharacterAndYear(Long characterId, Integer taxYear) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/tax/filings")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/character/{characterId}/year/{taxYear}", characterId, taxYear)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from JobService: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error getting tax filing for characterId={}, year={}: {}", characterId, taxYear, e.getMessage());
            return null;
        }
    }
}

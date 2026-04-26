package com.pet.businessdomain.systemservice.transactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.pet.businessdomain.shareddto.dto.*;
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
}

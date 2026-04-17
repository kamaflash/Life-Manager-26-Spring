package com.pet.businessdomain.eventservice.transactions;

import com.pet.businessdomain.shareddto.dto.*;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class BusinessTransactions {

    @Autowired
    private WebClient.Builder webClientBuilder;

    // URL base del microservicio de personaje
    @Value("${services.person-service.url:http://BUSINESSDOMAIN-PERSONSERVICE/api/characters}")
    private String personServiceUrl;

    // Configuración de HttpClient
    HttpClient client = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            .responseTimeout(Duration.ofSeconds(5))
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    public CharacterDto getPerson(Long id) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl(personServiceUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/id/full/{id}", id)
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
            System.err.println("Error fetching user: " + e.getMessage());
            return null;
        }
    }

    public CharacterDto updatePerson(CharacterDto character) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl(personServiceUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.put()
                    .uri("/stats/{id}", character.getId())
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

    /**
     * Actualiza los skills y estadísticas de un personaje al completar una formación
     */
    public CharacterSkillsUpdateResponseDto updateCharacterSkills(CharacterSkillsUpdateRequestDto request) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl(personServiceUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.post()
                    .uri("/skills/update")
                    .bodyValue(request)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from Person service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(CharacterSkillsUpdateResponseDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error updating character skills: " + e.getMessage());
            CharacterSkillsUpdateResponseDto errorResponse = new CharacterSkillsUpdateResponseDto();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error al actualizar skills: " + e.getMessage());
            return errorResponse;
        }
    }

    public SExpenseResponseDto setExpense(SExpenseResponseDto dto, Long accountId) {
        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FINANCESERVICE/api/expenses")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/{accountId}", accountId)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(SExpenseResponseDto.class)
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
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(NotificationDTO.class)
                .block();
    }

    public SystemDto getSystem(Long uid) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-SYSTEMSERVICE/api/systems")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/uid/{uid}", uid)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from System service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(SystemDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error fetching system: " + e.getMessage());
            return null;
        }
    }

    public SystemDto updateSystem(Long uid, LocalDateTime time, Integer pa) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-SYSTEMSERVICE/api/systems")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.put()
                    .uri("/{uid}/{pa}", uid, pa)
                    .bodyValue(time)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from System service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(SystemDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error updating system: " + e.getMessage());
            return null;
        }
    }
}
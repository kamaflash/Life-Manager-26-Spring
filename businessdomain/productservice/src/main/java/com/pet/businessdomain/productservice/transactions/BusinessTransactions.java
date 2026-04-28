package com.pet.businessdomain.productservice.transactions;

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

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class BusinessTransactions {
    // URL base del microservicio de personaje
    @Value("${services.person-service.url:http://BUSINESSDOMAIN-PERSONSERVICE/api/characters}")
    private String personServiceUrl;

    @Autowired
    private WebClient.Builder webClientBuilder;
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

    public CharacterDto getPerson(Long charecterId) {
        try {
            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-PERSONSERVICE/api/characters")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/id/full/{id}", charecterId)
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
    public CharacterDto updateCharacterStats(Long id, CharacterDto characterDto) {

        try {

            WebClient webClient = webClientBuilder
                    .clientConnector(new ReactorClientHttpConnector(client))
                    .baseUrl("http://BUSINESSDOMAIN-PERSONSERVICE/api/characters")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.put()
                    .uri("/stats/{id}", id)
                    .bodyValue(characterDto)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> Mono.error(new RuntimeException(
                                            "Error from Character service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(CharacterDto.class)
                    .block();

        } catch (Exception e) {
            System.err.println("Error updating character stats: " + e.getMessage());
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
    public SExpenseResponseDto setExpense(SExpenseResponseDto dto, Long accountId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FINANCESERVICE/api/expenses")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/{accountId}", accountId) // llamamos a /api/incomes/{accountId}
                .bodyValue(dto) // enviamos el DTO en el body
                .retrieve()
                .bodyToMono(SExpenseResponseDto.class) // esperamos un solo DTO
                .block(); // bloqueamos hasta recibir respuesta
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
                                            "Error from User service: " + response.statusCode() + " - " + body
                                    )))
                    )
                    .bodyToMono(SystemDto.class)
                    .block(); // devuelve UserDto directamente

        } catch (Exception e) {
            System.err.println("Error fetching user: " + e.getMessage());
            return null; // o lanza excepción, según tu diseño
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
                    .uri("/{uid}/{pa}", uid,pa)
                    .bodyValue(time) // 👈 enviamos el body
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

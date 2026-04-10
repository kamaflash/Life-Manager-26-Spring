package com.pet.businessdomain.personservice.transactions;

import com.pet.businessdomain.personservice.repository.CharacterRepository;
import com.pet.businessdomain.shareddto.dto.*;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BusinessTransactions {
    @Autowired
    private CharacterRepository characterRepository;

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

    public List<SFinanceAccountResponseDto> getAccount(Long ownerId) {

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
    public SFinanceAccountResponseDto setAccount(SFinanceAccountResponseDto dto, BigDecimal income, BigDecimal expense) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FINANCESERVICE/api/accounts")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/{income}/{expense}", income,expense) // llamamos a /api/incomes/{accountId}
                .bodyValue(dto) // enviamos el DTO en el body
                .retrieve()
                .bodyToMono(SFinanceAccountResponseDto.class) // esperamos un solo DTO
                .block(); // bloqueamos hasta recibir respuesta
    }
    public List<CharacterTrainingDto> getEducation(Long id) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/trainer")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/character/{id}")
                        .build(id))
                .retrieve()
                .bodyToFlux(CharacterTrainingDto.class)
                .collectList()
                .block();
    }
    public List<JobPositionDTO> getJobs(Long id) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE/api/jobs")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/positions/fulldto/{id}")
                        .build(id))
                .retrieve()
                .bodyToFlux(JobPositionDTO.class)
                .collectList()
                .block();
    }
    public List<CharacterInventoryResponseDTO> getInventory(Long characterId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-PRODUCTSERVICE/api/inventory")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{characterId}")
                        .build(characterId))
                .retrieve()
                .bodyToFlux(CharacterInventoryResponseDTO.class)
                .collectList()
                .block();
    }
    public List<CharacterJobDTO> getCharacterJobs(Long characterId) {
        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-JOBSERVICE")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri("/api/character-jobs/character/all/{characterId}", characterId)
                .retrieve()
                .bodyToFlux(CharacterJobDTO.class)
                .collectList()
                .block();
    }
    public List<CharacterInventoryResponseDTO> getInvetory(Long characterId) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-PRODUCTSERVICE/api/inventory")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri("/{characterId}", characterId)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException(
                                        "Error from Inventory service: " + response.statusCode() + " - " + body
                                )))
                )
                .bodyToFlux(CharacterInventoryResponseDTO.class)
                .collectList()
                .block();
    }
    public CharacterTrainingDto setEducation(CharacterTrainingDto dto, Long id) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FORMATIONSERVICE/api/trainer")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/subscribe/{id}", id)
                .bodyValue(dto) // enviamos el DTO en el body
                .retrieve()
                .bodyToMono(CharacterTrainingDto.class) // esperamos un solo DTO
                .block(); // bloqueamos hasta recibir respuesta
    }
    public SystemDto setSystem(SystemDto dto) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-SYSTEMSERVICE/api/systems")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/post")
                .bodyValue(dto) // enviamos el DTO en el body
                .retrieve()
                .bodyToMono(SystemDto.class) // esperamos un solo DTO
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

}
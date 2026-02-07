package com.pet.businessdomain.personservice.transactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.pet.businessdomain.personservice.dto.SCreateFinanceAccountRequestDto;
import com.pet.businessdomain.personservice.dto.SExpenseResponseDto;
import com.pet.businessdomain.personservice.dto.SFinanceAccountResponseDto;
import com.pet.businessdomain.personservice.dto.SIncomeResponseDto;
import com.pet.businessdomain.personservice.repository.CharacterRepository;
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
    public SIncomeResponseDto setIncome(Long accountId, SIncomeResponseDto dto) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FINANCESERVICE/api/incomes")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/{accountId}", accountId) // llamamos a /api/incomes/{accountId}
                .bodyValue(dto)                  // enviamos el DTO en el body
                .retrieve()
                .bodyToMono(SIncomeResponseDto.class) // esperamos un solo DTO de respuesta
                .block();                           // bloqueamos hasta recibir la respuesta
    }
    public SExpenseResponseDto setExpenses(Long accountId, SExpenseResponseDto dto) {

        WebClient webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://BUSINESSDOMAIN-FINANCESERVICE/api/expenses")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .uri("/{accountId}", accountId) // llamamos a /api/incomes/{accountId}
                .bodyValue(dto)                  // enviamos el DTO en el body
                .retrieve()
                .bodyToMono(SExpenseResponseDto.class) // esperamos un solo DTO de respuesta
                .block();                           // bloqueamos hasta recibir la respuesta
    }

}
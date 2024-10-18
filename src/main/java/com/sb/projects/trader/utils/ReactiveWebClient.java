package com.sb.projects.trader.utils;

import com.sb.projects.trader.DTO.BrokerErrorDTO;
import com.sb.projects.trader.DTO.DataTransferObject;
import com.sb.projects.trader.enums.ErrorCode;
import com.sb.projects.trader.exceptions.BrokerHttpException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class ReactiveWebClient<T extends DataTransferObject, U extends DataTransferObject, E extends BrokerErrorDTO> {
    private final WebClient webClient;
    private final Class<T> clazz;
    private final Class<U> clazzU;
    private final Class<E> clazzE;

    public Mono<T> post(String uri, Map<String, String> headers, U body){
        log.info("Requesting remote broker at '{}' with headers: {} and body: {}", uri, headers, body.toString());
        Mono<U> bodyMono = Mono.just(body);
        return webClient
                .post()
                .uri(uri)
                .headers((httpHeaders) -> {
                    headers.forEach(httpHeaders::add);
                })
                .body(bodyMono, clazzU)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (res) -> res.bodyToMono(BrokerErrorDTO.class).handle((b, handler) -> {
                    log.error("Error response for POST uri '{}': {}", uri, b);
                    handler.error(new BrokerHttpException(HttpStatus.BAD_REQUEST,
                            ErrorCode.RemoteIOError,
                            String.format("%s Bad request returned by remote broker","res.statusCode()"),
                            null, b));
                }))
                .bodyToMono(clazz);
    }

    public Mono<T> get(String uri, Map<String, String> headers, Map<String, String> queryParams) {
        return webClient.get()
                .uri(uriBuilder -> {
                    queryParams.forEach(uriBuilder::queryParam);
                    return uriBuilder.path(uri).build();
                })
                .headers(httpHeaders -> headers.forEach(httpHeaders::add))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (res) -> res.bodyToMono(BrokerErrorDTO.class).handle((b, handler) -> {
                    log.error("Error response for GET uri '{}': {}", uri, b);
                    handler.error(new BrokerHttpException(HttpStatus.BAD_REQUEST,
                            ErrorCode.RemoteIOError,
                            String.format("%s Bad request returned by remote broker", "res.statusCode()"),
                            null, b));
                }))
                .bodyToMono(clazz);
    }
}

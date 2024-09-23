package com.sb.projects.trader.utils;

import com.sb.projects.trader.DTO.DataTransferObject;
import lombok.AllArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
public class ReactiveWebClient<T extends DataTransferObject, U extends DataTransferObject> {
    private final WebClient webClient;
    private final Class<T> clazz;
    private final Class<U> clazzU;

    public Mono<T> call(String uri, Map<String, String> headers, U body){
        Mono<U> bodyMono = Mono.just(body);
        return webClient
                .post()
                .uri(uri)
                .headers((httpHeaders) -> {
                    headers.forEach(httpHeaders::add);
                })
                .body(bodyMono, clazzU)
                .retrieve()
                .bodyToMono(clazz);
    }
}

package com.sb.projects.trader.service.paytm;

import com.sb.projects.trader.DTO.BrokerTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmErrorDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenDTO;
import com.sb.projects.trader.DTO.paytm.PaytmTokenRequestDTO;
import com.sb.projects.trader.entity.Token;
import com.sb.projects.trader.enums.TokenIssuer;
import com.sb.projects.trader.enums.TokenType;
import com.sb.projects.trader.repository.TokenRepository;
import com.sb.projects.trader.service.BrokerTokenService;
import com.sb.projects.trader.utils.ReactiveWebClient;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class PaytmTokenService implements BrokerTokenService<BrokerTokenDTO> {

    private final String apiKey;
    private final String apiSecret;
    private final String requestToken;
    private final ReactiveWebClient<PaytmTokenDTO, PaytmTokenRequestDTO, PaytmErrorDTO> reactiveWebClient;
    private final TokenRepository tokenRepository;
    private final String uri = "accounts/v2/gettoken";

    @Override
    public Mono<BrokerTokenDTO> getToken() {
        String accessToken = getCachedToken(TokenType.accessToken);
        if (StringUtil.isNullOrEmpty(accessToken)) {
            Map<String, String> headers = new HashMap<>();
            headers.put("Host", "developer.paytmmoney.com");

            return reactiveWebClient
                    .call(uri, headers, PaytmTokenRequestDTO.builder()
                            .apiKey(apiKey)
                            .apiSecret(apiSecret)
                            .requestToken(getCachedToken(TokenType.requestToken)).build())
                    .map(dto -> {
                        log.info("Remote token response: {}", dto.toString());
                        cacheToken(TokenType.accessToken, dto.getPaytmAccessToken());
                        return new BrokerTokenDTO(dto.getPaytmAccessToken());
                    });
        } else return Mono.just(new BrokerTokenDTO((accessToken)));
    }

    private void deleteToken(TokenType tokenType){
        tokenRepository.deleteByTokenType(tokenType);
    }

    private void cacheToken(TokenType tokenType, String token){
        Token tokenEntity = new Token();
        tokenEntity.setTokenType(tokenType);
        tokenEntity.setTokenIssuer(TokenIssuer.Paytm);
        tokenEntity.setToken(token);
        tokenEntity.setIssuedAt(LocalDateTime.now());
        tokenEntity.setExpiresAt(LocalDateTime.now().plusHours(8));

        tokenRepository.save(tokenEntity);
    }

    private void updateCachedToken(Token token){
        tokenRepository.save(token);
    }

    private String getCachedToken(TokenType tokenType){
        if (tokenType == TokenType.accessToken){
            Optional<Token> token = tokenRepository.findByTokenTypeAndTokenIssuer(
                    TokenType.accessToken, TokenIssuer.Paytm);

            if (token.isPresent())
                if (token.get().getExpiresAt().isAfter(LocalDateTime.now())) {
                    return token.get().getToken();
                }

        } else if (tokenType == TokenType.requestToken) {
            Optional<Token> token = tokenRepository.findByTokenTypeAndTokenIssuer(
                    TokenType.requestToken, TokenIssuer.Paytm);

            if (token.isPresent()) {
                return token.get().getToken();
            } else {
                cacheToken(TokenType.requestToken, requestToken);
                return requestToken;
            }
        }

        return null;
    }
}

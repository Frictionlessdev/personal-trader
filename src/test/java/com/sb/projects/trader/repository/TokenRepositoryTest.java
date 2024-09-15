package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Token;
import com.sb.projects.trader.enums.TokenIssuer;
import com.sb.projects.trader.enums.TokenType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:dcbapp",
        "spring.jps.hibernate.ddl-auto=create-drop"}, showSql = true)
class TokenRepositoryTest {

    @Autowired
    TokenRepository tokenRepository;

    Token refreshToken;

    @BeforeEach
    void setUp(){
        Token token = new Token();
        token.setTokenType(TokenType.requestToken);
        token.setTokenIssuer(TokenIssuer.Paytm);
        token.setToken("testtokenvalue");

        refreshToken = tokenRepository.save(token);
    }

    @Test
    void givenToken_whenSaved_canBeFoundByType(){
        tokenRepository.findByTokenType(TokenType.requestToken);
    }

    @AfterEach
    void deleteToken(){
        tokenRepository.delete(refreshToken);
    }
}
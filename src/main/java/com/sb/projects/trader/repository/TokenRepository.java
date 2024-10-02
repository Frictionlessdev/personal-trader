package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Token;
import com.sb.projects.trader.enums.TokenIssuer;
import com.sb.projects.trader.enums.TokenType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Token> {
    Optional<Token> findByTokenType(TokenType tokenType);
    Optional<Token> findByTokenTypeAndTokenIssuer(TokenType tokenType, TokenIssuer tokenIssuer);
    Token deleteByTokenType(TokenType tokenType);
}

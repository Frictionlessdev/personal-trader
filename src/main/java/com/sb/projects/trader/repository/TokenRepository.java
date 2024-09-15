package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Token;
import com.sb.projects.trader.enums.TokenType;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Token> {
    Token findByTokenType(TokenType tokenType);
}

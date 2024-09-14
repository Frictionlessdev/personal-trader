package com.sb.projects.trader.repository;

import com.sb.projects.trader.entity.Security;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:dcbapp",
        "spring.jps.hibernate.ddl-auto=create-drop"
}, showSql = true)
public class SecurityRepositoryTest {

    @Autowired
    SecurityRepository securityRepository;

    private Security security;

    @Test
    void givenSecurity_whenSaved_thenCanBeFoundById(){
        security = securityRepository.findBySecurityId("19084");
    }

    @AfterEach
    public void tearDown(){
        securityRepository.delete(security);
    }
}
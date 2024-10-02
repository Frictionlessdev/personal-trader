package com.sb.projects.trader.entity;

import com.sb.projects.trader.enums.TokenIssuer;
import com.sb.projects.trader.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "token")
@DynamicUpdate
public class Token implements JpaEntityObject{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private String id;
    private TokenIssuer tokenIssuer;
    private TokenType tokenType;
    private String token;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
}

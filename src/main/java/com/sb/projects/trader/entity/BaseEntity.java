package com.sb.projects.trader.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity implements JpaEntityObject{

    @Column(name = "createdAt", nullable=true)
    private LocalDateTime created;

    @Column(name = "updatedAt", nullable=true)
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate(){
        updated = created = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        updated = LocalDateTime.now();
    }
}

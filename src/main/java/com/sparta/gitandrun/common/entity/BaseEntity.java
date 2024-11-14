package com.sparta.gitandrun.common.entity;

import com.sparta.gitandrun.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

    @Column
    private String createdBy;

    @Column
    private String updatedBy;
    @Column
    private String deletedBy;

    protected void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    protected void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    protected void initAuditInfo(User user) {
        this.createdBy = user.getUsername();
        this.updatedBy = user.getUsername();
    }
}
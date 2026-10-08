package com.bright.thought.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "thoughts")
@Entity
public class ThoughtEntity {
    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "situation_id", nullable = false)
    private SituationEntity situation;

    @Column(name = "critical_content", nullable = false)
    private String criticalContent;

    @Column(name = "real_content")
    private String realContent;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    public ThoughtEntity() {}

    public ThoughtEntity(
            SituationEntity situation,
            String criticalContent,
            String realContent
    ) {
        this.situation = situation;
        this.criticalContent = criticalContent;
        this.realContent = realContent;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) { this.id = id; }

    public SituationEntity getSituation() {
        return situation;
    }
    public void setSituation(SituationEntity situation) { this.situation = situation; }

    public String getCriticalContent() {
        return criticalContent;
    }
    public void setCriticalContent(String criticalContent) {
        this.criticalContent = criticalContent;
    }

    public String getRealContent() {
        return realContent;
    }
    public void setRealContent(String realContent) {
        this.realContent = realContent;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}

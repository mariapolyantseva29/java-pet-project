package com.bright.thought.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ThoughtRequest {
    private UUID id;

    private UUID situationId;

    private String criticalContent;

    private String realContent;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public ThoughtRequest() {}

    public ThoughtRequest(UUID situationId, String criticalContent, String realContent) {
        this.situationId = situationId;
        this.criticalContent = criticalContent;
        this.realContent = realContent;
    }

    public ThoughtRequest(UUID id, UUID situationId, String criticalContent, String realContent, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.situationId = situationId;
        this.criticalContent = criticalContent;
        this.realContent = realContent;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

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

    public UUID getSituationId() {
        return situationId;
    }
    public void setSituationId(UUID situationId) { this.situationId = situationId; }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(LocalDateTime updateDate) { this.updateDate = updateDate; }
}

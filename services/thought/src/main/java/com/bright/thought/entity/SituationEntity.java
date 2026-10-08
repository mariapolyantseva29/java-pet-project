package com.bright.thought.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "situations")
@Entity
public class SituationEntity {
    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(name = "create_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name = "update_date", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateDate;

    @OneToMany(
            mappedBy = "situation",
            cascade = {CascadeType.REMOVE, CascadeType.PERSIST},
            orphanRemoval = true
    )
    private List<ThoughtEntity> thoughts = new ArrayList<>();

    public SituationEntity() {}

    public SituationEntity(
            UUID userId,
            String description
    ) {
        this.userId = userId;
        this.description = description;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    public LocalDateTime getUpdateDate() { return updateDate; }
    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public List<ThoughtEntity> getThoughts() {
        return thoughts;
    }
    public void setThoughts(List<ThoughtEntity> thoughts) { this.thoughts = thoughts; }

    public void addThought(ThoughtEntity thought) {
        thoughts.add(thought);
    }
}

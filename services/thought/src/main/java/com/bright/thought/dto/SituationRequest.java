package com.bright.thought.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SituationRequest {
    private UUID id;

    @NotBlank(message = "Description can not be empty")
    @NotNull(message = "Description can not be null")
    @Size(min = 3, max = 500, message = "Description must be from 3 to 500 symbols")
    private String description;

    @Valid
    private List<ThoughtRequest> thoughts = new ArrayList<ThoughtRequest>();

    public SituationRequest() {};
    public SituationRequest(UUID id, String description, List<ThoughtRequest> thoughts) {
        this.id = id;
        this.description = description;
        this.thoughts = thoughts;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) { this.id = id; }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<ThoughtRequest> getThoughts() {
        return thoughts;
    }
    public void setThoughts(List<ThoughtRequest> thoughts) { this.thoughts = thoughts; }
}

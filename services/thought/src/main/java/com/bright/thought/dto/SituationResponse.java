package com.bright.thought.dto;

import com.bright.thought.dto.enums.Status;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SituationResponse {
    @NotNull
    private List<SituationRequest> situations;

    @NotNull
    private Class<Status> status;

    @NotNull
    private String type;

    public SituationResponse(List<SituationRequest> situations, Class<Status> status, String type) {
        this.situations = situations;
        this.status = status;
        this.type = type;
    }

    public List<SituationRequest> getSituations() {
        return situations;
    }

    public Class<Status> getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}

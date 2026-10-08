package com.bright.thought.controller;

import com.bright.thought.dto.SituationRequest;
import com.bright.thought.dto.SituationResponse;
import com.bright.thought.dto.enums.Status;
import com.bright.thought.security.AppUser;
import com.bright.thought.service.KafkaLogProducer;
import com.bright.thought.service.SituationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/situations")
public class SituationsController {
    private final SituationService service;
    private final KafkaLogProducer kafkaLogProducer;

    public SituationsController(SituationService service, KafkaLogProducer kafkaLogProducer) {
        this.service = service;
        this.kafkaLogProducer = kafkaLogProducer;
    }

    @GetMapping
    public ResponseEntity<SituationResponse> getSituations(
            @AuthenticationPrincipal AppUser user
    ) {
        kafkaLogProducer.sendLog("INFO", "Received getSituations with user id: " + user.getId());
        List<SituationRequest> situations = service.getAllSituations(user.getId());

        SituationResponse situationResponse = new SituationResponse(situations, Status.class, "all");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(situationResponse);
    }

    @GetMapping("/edited")
    public ResponseEntity<SituationResponse> getEditedSituations(
            @AuthenticationPrincipal AppUser user
    ) {
        kafkaLogProducer.sendLog("INFO", "Received getEditedSituations with user id: " + user.getId());
        List<SituationRequest> situations = service.getEditedSituations(user.getId());

        SituationResponse situationResponse = new SituationResponse(situations, Status.class, "edited");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(situationResponse);
    }

    @GetMapping("/done")
    public ResponseEntity<SituationResponse> getDoneSituations(
            @AuthenticationPrincipal AppUser user
    ) {
        kafkaLogProducer.sendLog("INFO", "Received getDoneSituations with user id: " + user.getId());
        List<SituationRequest> situations = service.getDoneSituations(user.getId());

        SituationResponse situationResponse = new SituationResponse(situations, Status.class, "done");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(situationResponse);
    }

    @PreAuthorize("@securityService.isUserOwner(#id, #user.id)")
    @GetMapping("/{id}")
    public ResponseEntity<SituationRequest> getSituation(
            @AuthenticationPrincipal AppUser user,
            @PathVariable("id") UUID id
    ) {
        kafkaLogProducer.sendLog("INFO", "Received getSituation with situation id: " + id);
        SituationRequest situationRequest = service.getSituationById(id);

        return ResponseEntity.status(HttpStatus.OK).body(situationRequest);
    }

    @PostMapping
    public ResponseEntity<SituationRequest> createSituation(
            @AuthenticationPrincipal AppUser user,
            @RequestBody SituationRequest situationToCreate
    ) {
        kafkaLogProducer.sendLog("INFO", "Received createSituation with user id: " + user.getId());
        SituationRequest situation = service.createSituation(user.getId(), situationToCreate);

        return ResponseEntity.status(HttpStatus.OK).body(situation);
    }

    @PreAuthorize("@securityService.isUserOwner(#id, #user.id)")
    @PostMapping("/{id}")
    public ResponseEntity<SituationRequest> updateSituation(
            @AuthenticationPrincipal AppUser user,
            @PathVariable("id") UUID id,
            @RequestBody SituationRequest situationRequest
    ) {
        kafkaLogProducer.sendLog("INFO", "Received updateSituation with situation id: " + id);
        SituationRequest updatedSituation = service.updateSituation(id, situationRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedSituation);
    }
}

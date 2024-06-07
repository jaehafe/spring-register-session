package org.boot.registersession.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.boot.registersession.model.sessionspeaker.SessionSpeaker;
import org.boot.registersession.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import org.boot.registersession.model.sessionspeaker.SessionSpeakerPostRequestBody;
import org.boot.registersession.service.SessionSpeakerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/session-speakers")
public class SessionSpeakerController {

    private final SessionSpeakerService sessionSpeakerService;

    public SessionSpeakerController(SessionSpeakerService sessionSpeakerService) {
        this.sessionSpeakerService = sessionSpeakerService;
    }

    @GetMapping
    public ResponseEntity<List<SessionSpeaker>> getSessionSpeakers() {
        var sessionSpeakers = sessionSpeakerService.getSessionSpeakers();
        return ResponseEntity.ok(sessionSpeakers);
    }

    @GetMapping("/{speakerId}")
    public ResponseEntity<SessionSpeaker> getSessionSpeakerBySpeakerId(
            @PathVariable(name = "speakerId") Long speakerId
    ) {
        var sessionSpeaker = sessionSpeakerService.getSessionSpeakerBySpeakerId(speakerId);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @PostMapping
    public ResponseEntity<SessionSpeaker> createSessionSpeaker(
            @Valid @RequestBody SessionSpeakerPostRequestBody sessionSpeakerRequestBody
    ) {
        var sessionSpeaker = sessionSpeakerService.createSessionSpeaker(sessionSpeakerRequestBody);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @PatchMapping("/{speakerId}")
    public ResponseEntity<SessionSpeaker> updateSessionSpeaker(
            @PathVariable(name = "speakerId") Long speakerId,
            @Valid @RequestBody SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody
    ) {
        SessionSpeaker sessionSpeaker = sessionSpeakerService.updateSessionSpeaker(speakerId,
                sessionSpeakerPatchRequestBody);
        return ResponseEntity.ok(sessionSpeaker);
    }

    @DeleteMapping("/{speakerId}")
    public ResponseEntity<Void> deleteSessionSpeaker(@PathVariable(name = "speakerId") Long speakerId) {
        sessionSpeakerService.deleteSessionSpeaker(speakerId);
        return ResponseEntity.noContent().build();
    }
}

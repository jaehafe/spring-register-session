package org.boot.registersession.controller;


import jakarta.validation.Valid;
import java.util.List;
import org.boot.registersession.model.crashsession.CrashSession;
import org.boot.registersession.model.crashsession.CrashSessionPatchRequestBody;
import org.boot.registersession.model.crashsession.CrashSessionPostRequestBody;
import org.boot.registersession.model.crashsession.CrashSessionRegistrationStatus;
import org.boot.registersession.model.entity.UserEntity;
import org.boot.registersession.service.CrashSessionService;
import org.boot.registersession.service.RegistrationService;
import org.boot.registersession.service.SlackService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/crash-sessions")
public class CrashSessionController {

    private final CrashSessionService crashSessionService;
    private final RegistrationService registrationService;

    public CrashSessionController(CrashSessionService crashSessionService, RegistrationService registrationService) {
        this.crashSessionService = crashSessionService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public ResponseEntity<List<CrashSession>> getCrashSessions() {
        List<CrashSession> crashSessions = crashSessionService.getCrashSessions();
        return ResponseEntity.ok(crashSessions);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<CrashSession> getCrashSessionBySessionId(
            @PathVariable(name = "sessionId") Long sessionId) {
        CrashSession crashSession = crashSessionService.getCrashSessionBySessionId(sessionId);
        return ResponseEntity.ok(crashSession);
    }

    @GetMapping("/{sessionId}/registration-status")
    public ResponseEntity<CrashSessionRegistrationStatus> getCrashSessionRegistrationStatusBySessionId(
            @PathVariable(name = "sessionId") Long sessionId,
            Authentication authentication) {
        CrashSessionRegistrationStatus registrationStatus = registrationService.getCrashSessionRegistrationStatusBySessionIdAndCurrentUser(
                sessionId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(registrationStatus);
    }

    @PostMapping
    public ResponseEntity<CrashSession> createCrashSession(
            @Valid @RequestBody CrashSessionPostRequestBody crashSessionPostRequestBody
    ) {
        CrashSession crashSession = crashSessionService.createCrashSession(crashSessionPostRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @PatchMapping("/{sessionId}")
    public ResponseEntity<CrashSession> updateCrashSession(
            @PathVariable(name = "sessionId") Long sessionId,
            @RequestBody CrashSessionPatchRequestBody crashSessionPatchRequestBody
    ) {
        CrashSession crashSession = crashSessionService.updateCrashSession(sessionId, crashSessionPatchRequestBody);
        return ResponseEntity.ok(crashSession);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteCrashSession(
            @PathVariable(name = "sessionId") Long sessionId
    ) {
        crashSessionService.deleteCrashSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}

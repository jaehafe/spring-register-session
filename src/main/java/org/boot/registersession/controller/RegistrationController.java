package org.boot.registersession.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.boot.registersession.model.entity.UserEntity;
import org.boot.registersession.model.registration.Registration;
import org.boot.registersession.model.registration.RegistrationPostRequestBody;
import org.boot.registersession.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public ResponseEntity<List<Registration>> getRegistrations(Authentication authentication) {
        List<Registration> registrations = registrationService.getRegistrationsByCurrentUser(
                (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(registrations);
    }

    @GetMapping("/{registrationId}")
    public ResponseEntity<Registration> getRegistrationByRegistrationId(
            @PathVariable(name = "registrationId") Long registrationId,
            Authentication authentication
    ) {
        Registration registration = registrationService.getRegistrationsByRegistrationIdByCurrentUser(
                registrationId, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(registration);
    }

    @PostMapping
    public ResponseEntity<Registration> createRegistration(
            @Valid @RequestBody RegistrationPostRequestBody registrationPostRequestBody,
            Authentication authentication
    ) {
        Registration registration = registrationService.createRegistrationByCurrentUser(
                registrationPostRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(registration);
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<Void> deleteRegistration(
            @PathVariable(name = "registrationId") Long registrationId,
            Authentication authentication) {
        registrationService.deleteRegistrationByRegistrationIdAndCurrentUser(registrationId,
                (UserEntity) authentication.getPrincipal());
        return ResponseEntity.noContent().build();
    }
}

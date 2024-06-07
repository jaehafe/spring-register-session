package org.boot.registersession.service;

import java.util.List;
import java.util.Optional;
import org.boot.registersession.exception.registration.RegistrationAlreadyExistsException;
import org.boot.registersession.exception.registration.RegistrationNotFoundException;
import org.boot.registersession.model.crashsession.CrashSessionRegistrationStatus;
import org.boot.registersession.model.entity.CrashSessionEntity;
import org.boot.registersession.model.entity.RegistrationEntity;
import org.boot.registersession.model.entity.UserEntity;
import org.boot.registersession.model.registration.Registration;
import org.boot.registersession.model.registration.RegistrationPostRequestBody;
import org.boot.registersession.repository.RegistrationEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final RegistrationEntityRepository registrationEntityRepository;
    private final CrashSessionService crashSessionService;

    public RegistrationService(RegistrationEntityRepository registrationEntityRepository,
            CrashSessionService crashSessionService) {
        this.registrationEntityRepository = registrationEntityRepository;
        this.crashSessionService = crashSessionService;
    }


    public List<Registration> getRegistrationsByCurrentUser(UserEntity currentUser) {

        List<RegistrationEntity> registrationEntities = registrationEntityRepository.findByUser(
                currentUser);
        return registrationEntities.stream().map(Registration::from).toList();
    }

    public Registration getRegistrationsByRegistrationIdByCurrentUser(Long registrationId,
            UserEntity currentUser) {

        RegistrationEntity registrationEntity = getRegistrationEntityByRegistrationIdAndUserEntity(
                registrationId, currentUser);
        return Registration.from(registrationEntity);
    }

    public RegistrationEntity getRegistrationEntityByRegistrationIdAndUserEntity(
            Long registrationId, UserEntity userEntity) {

        return registrationEntityRepository.findByRegistrationIdAndUser(registrationId,
                        userEntity)
                .orElseThrow(() -> new RegistrationNotFoundException(registrationId, userEntity));
    }

    public Registration createRegistrationByCurrentUser(
            RegistrationPostRequestBody registrationPostRequestBody, UserEntity currentUser) {
        CrashSessionEntity crashSessionEntity =
                crashSessionService.getCrashSessionEntityBySessionId(
                        registrationPostRequestBody.sessionId()
                );

        registrationEntityRepository.findByUserAndSession(currentUser, crashSessionEntity)
                .ifPresent(registrationEntity -> {
                    throw new RegistrationAlreadyExistsException(crashSessionEntity.getSessionId(),
                            currentUser);
                });

        RegistrationEntity registrationEntity = RegistrationEntity.of(currentUser, crashSessionEntity);
        return Registration.from(registrationEntityRepository.save(registrationEntity));
    }

    public void deleteRegistrationByRegistrationIdAndCurrentUser(Long registrationId, UserEntity currentUser) {
        RegistrationEntity registrationEntity =
                getRegistrationEntityByRegistrationIdAndUserEntity(registrationId, currentUser);
        registrationEntityRepository.delete(registrationEntity);
    }

    public CrashSessionRegistrationStatus getCrashSessionRegistrationStatusBySessionIdAndCurrentUser(
            Long sessionId, UserEntity currentUser) {

        CrashSessionEntity crashSessionEntity = crashSessionService.getCrashSessionEntityBySessionId(sessionId);
        Optional<RegistrationEntity> registrationEntity = registrationEntityRepository.findByUserAndSession(
                currentUser, crashSessionEntity);

        return new CrashSessionRegistrationStatus(
                sessionId,
                registrationEntity.isPresent(),
                registrationEntity.map(RegistrationEntity::getRegistrationId).orElse(null)
        );
    }
}

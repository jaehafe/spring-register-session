package org.boot.registersession.service;

import java.util.List;
import org.boot.registersession.exception.crashsession.CrashSessionNotFoundException;
import org.boot.registersession.model.crashsession.CrashSession;
import org.boot.registersession.model.crashsession.CrashSessionPatchRequestBody;
import org.boot.registersession.model.crashsession.CrashSessionPostRequestBody;
import org.boot.registersession.model.entity.CrashSessionEntity;
import org.boot.registersession.model.entity.SessionSpeakerEntity;
import org.boot.registersession.repository.CrashSessionCacheRepository;
import org.boot.registersession.repository.CrashSessionEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class CrashSessionService {

    private final CrashSessionEntityRepository crashSessionEntityRepository;
    private final SessionSpeakerService sessionSpeakerService;
    private final CrashSessionCacheRepository crashSessionCacheRepository;

    public CrashSessionService(CrashSessionEntityRepository crashSessionEntityRepository,
            SessionSpeakerService sessionSpeakerService,
            CrashSessionCacheRepository crashSessionCacheRepository) {
        this.crashSessionEntityRepository = crashSessionEntityRepository;
        this.sessionSpeakerService = sessionSpeakerService;
        this.crashSessionCacheRepository = crashSessionCacheRepository;
    }

    public List<CrashSession> getCrashSessions() {
        return crashSessionEntityRepository.findAll().stream()
                .map(CrashSession::from)
                .toList();
    }

    public CrashSession getCrashSessionBySessionId(Long sessionId) {
        return crashSessionCacheRepository
                .getCrashSessionCache(sessionId)
                .orElseGet(
                        () -> {
                            var crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
                            var crashSession = CrashSession.from(crashSessionEntity);
                            crashSessionCacheRepository.setCrashSessionCache(crashSession);
                            return crashSession;
                        });
    }

    public CrashSession createCrashSession(
            CrashSessionPostRequestBody crashSessionPostRequestBody) {
        SessionSpeakerEntity sessionSpeakerEntity =
                sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
                        crashSessionPostRequestBody.speakerId());

        CrashSessionEntity crashSessionEntity = CrashSessionEntity.of(
                crashSessionPostRequestBody.title(),
                crashSessionPostRequestBody.body(),
                crashSessionPostRequestBody.category(),
                crashSessionPostRequestBody.dateTime(),
                sessionSpeakerEntity
        );

        return CrashSession.from(crashSessionEntityRepository.save(crashSessionEntity));
    }

    public CrashSession updateCrashSession(Long sessionId,
            CrashSessionPatchRequestBody crashSessionPatchRequestBody) {

        CrashSessionEntity crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.title())) {
            crashSessionEntity.setTitle(crashSessionPatchRequestBody.title());
        }

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.body())) {
            crashSessionEntity.setBody(crashSessionPatchRequestBody.body());
        }

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.category())) {
            crashSessionEntity.setCategory(crashSessionPatchRequestBody.category());
        }

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.dateTime())) {
            crashSessionEntity.setDateTime(crashSessionPatchRequestBody.dateTime());
        }

        if (!ObjectUtils.isEmpty(crashSessionPatchRequestBody.speakerId())) {
            SessionSpeakerEntity sessionSpeakerEntity =
                    sessionSpeakerService.getSessionSpeakerEntityBySpeakerId(
                            crashSessionPatchRequestBody.speakerId()
                    );
            crashSessionEntity.setSpeaker(sessionSpeakerEntity);
        }

        return CrashSession.from(crashSessionEntityRepository.save(crashSessionEntity));
    }

    public void deleteCrashSession(Long sessionId) {
        CrashSessionEntity crashSessionEntity = getCrashSessionEntityBySessionId(sessionId);
        crashSessionEntityRepository.delete(crashSessionEntity);

    }

    public CrashSessionEntity getCrashSessionEntityBySessionId(Long sessionId) {
        return crashSessionEntityRepository.findById(sessionId)
                .orElseThrow(() -> new CrashSessionNotFoundException(sessionId));
    }
}

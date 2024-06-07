package org.boot.registersession.service;

import java.util.List;
import org.boot.registersession.exception.sessionspeaker.SessionSpeakerNotFoundException;
import org.boot.registersession.model.entity.SessionSpeakerEntity;
import org.boot.registersession.model.sessionspeaker.SessionSpeaker;
import org.boot.registersession.model.sessionspeaker.SessionSpeakerPatchRequestBody;
import org.boot.registersession.model.sessionspeaker.SessionSpeakerPostRequestBody;
import org.boot.registersession.repository.SessionSpeakerEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class SessionSpeakerService {

    private final SessionSpeakerEntityRepository sessionSpeakerEntityRepository;

    public SessionSpeakerService(SessionSpeakerEntityRepository sessionSpeakerEntityRepository) {
        this.sessionSpeakerEntityRepository = sessionSpeakerEntityRepository;
    }

    public List<SessionSpeaker> getSessionSpeakers() {
        List<SessionSpeakerEntity> sessionSpeakerEntities = sessionSpeakerEntityRepository.findAll();
        return sessionSpeakerEntities.stream().map(SessionSpeaker::from).toList();
    }

    public SessionSpeakerEntity getSessionSpeakerEntityBySpeakerId(Long speakerId) {
        return sessionSpeakerEntityRepository.findById(speakerId)
                .orElseThrow(() -> new SessionSpeakerNotFoundException(speakerId));
    }

    public SessionSpeaker getSessionSpeakerBySpeakerId(Long speakerId) {
        var sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);
        return SessionSpeaker.from(sessionSpeakerEntity);
    }

    public SessionSpeaker createSessionSpeaker(
            SessionSpeakerPostRequestBody sessionSpeakerRequestBody) {
        var sessionSpeakerEntity =
                SessionSpeakerEntity.of(
                        sessionSpeakerRequestBody.company(),
                        sessionSpeakerRequestBody.name(),
                        sessionSpeakerRequestBody.description()
                );

        return SessionSpeaker.from(sessionSpeakerEntityRepository.save(sessionSpeakerEntity));
    }

    public SessionSpeaker updateSessionSpeaker(Long speakerId,
            SessionSpeakerPatchRequestBody sessionSpeakerPatchRequestBody) {
        SessionSpeakerEntity sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);

        if(!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.company())) {
            sessionSpeakerEntity.setCompany(sessionSpeakerPatchRequestBody.company());
        }

        if(!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.name())) {
            sessionSpeakerEntity.setName(sessionSpeakerPatchRequestBody.name());
        }

        if(!ObjectUtils.isEmpty(sessionSpeakerPatchRequestBody.description())) {
            sessionSpeakerEntity.setDescription(sessionSpeakerPatchRequestBody.description());
        }

        return SessionSpeaker.from(
                sessionSpeakerEntityRepository.save(sessionSpeakerEntity)
        );
    }

    public void deleteSessionSpeaker(Long speakerId) {
        SessionSpeakerEntity sessionSpeakerEntity = getSessionSpeakerEntityBySpeakerId(speakerId);
        sessionSpeakerEntityRepository.delete(sessionSpeakerEntity);
    }
}

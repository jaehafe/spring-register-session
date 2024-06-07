package org.boot.registersession.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;
import org.boot.registersession.model.crashsession.CrashSessionCategory;

@Entity
@Table(name = "crashsession")
@Getter
@Setter
public class CrashSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CrashSessionCategory category;

    @Column(nullable = false)
    private ZonedDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "speakerid")
    private SessionSpeakerEntity speaker;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CrashSessionEntity that = (CrashSessionEntity) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(title,
                that.title) && Objects.equals(body, that.body) && category == that.category
                && Objects.equals(dateTime, that.dateTime) && Objects.equals(
                speaker, that.speaker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, title, body, category, dateTime, speaker);
    }

    public static CrashSessionEntity of(
            String title,
            String body,
            CrashSessionCategory crashSessionCategory,
            ZonedDateTime dateTime,
            SessionSpeakerEntity sessionSpeakerEntity
    ) {
        CrashSessionEntity crashSessionEntity = new CrashSessionEntity();
        crashSessionEntity.setTitle(title);
        crashSessionEntity.setBody(body);
        crashSessionEntity.setCategory(crashSessionCategory);
        crashSessionEntity.setDateTime(dateTime);
        crashSessionEntity.setSpeaker(sessionSpeakerEntity);
        return crashSessionEntity;
    }
}

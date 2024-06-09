package org.boot.registersession.repository;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.boot.registersession.model.crashsession.CrashSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

@Repository
public class CrashSessionCacheRepository {

    private final RedisTemplate<String, CrashSession> crashSessionRedisTemplate;
    private final RedisTemplate<String, List<CrashSession>> crashSessionsListRedisTemplate;

    public CrashSessionCacheRepository(
            @Qualifier("crashSessionRedisTemplate")
            RedisTemplate<String, CrashSession> crashSessionRedisTemplate,
            @Qualifier("crashSessionsListRedisTemplate")
            RedisTemplate<String, List<CrashSession>> crashSessionsListRedisTemplate
    ) {
        this.crashSessionRedisTemplate = crashSessionRedisTemplate;
        this.crashSessionsListRedisTemplate = crashSessionsListRedisTemplate;
    }

    public void setCrashSessionCache(CrashSession crashSession) {
        var redisKey = getRedisKey(crashSession.sessionId());
        crashSessionRedisTemplate.opsForValue().set(redisKey, crashSession, Duration.ofSeconds(30));
    }

    public void setCrashSessionsListCache(List<CrashSession> crashSessions) {
        crashSessionsListRedisTemplate.opsForValue().set("sessions", crashSessions);
    }

    public Optional<CrashSession> getCrashSessionCache(Long sessionId) {
        var redisKey = getRedisKey(sessionId);
        var crashSession = crashSessionRedisTemplate.opsForValue().get(redisKey);
        return Optional.ofNullable(crashSession);
    }

    public List<CrashSession> getCrashSessionsListCache() {
        var crashSessions = crashSessionsListRedisTemplate.opsForValue().get("sessions");
        if (ObjectUtils.isEmpty(crashSessions)) {
            return Collections.emptyList();
        }
        return crashSessions;
    }

    private String getRedisKey(Long sessionId) {
        return "session:" + sessionId;
    }
}

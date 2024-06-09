package org.boot.registersession.repository;

import java.time.Duration;
import java.util.Optional;
import org.boot.registersession.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserEntityCacheRepository {

    private final RedisTemplate<String, UserEntity> userEntityRedisTemplate;

    public UserEntityCacheRepository(@Qualifier("userEntityRedisTemplate") RedisTemplate<String, UserEntity> userEntityRedisTemplate) {
        this.userEntityRedisTemplate = userEntityRedisTemplate;
    }

    public void setUserEntityCache(UserEntity userEntity) {
        String redisKey = getRedisKey(userEntity.getUsername());
        userEntityRedisTemplate.opsForValue().set(redisKey, userEntity, Duration.ofSeconds(30));
    }

    public Optional<UserEntity> getUserEntityCache(String username) {
        String redisKey = getRedisKey(username);
        UserEntity userEntity = userEntityRedisTemplate.opsForValue().get(redisKey);
        return Optional.ofNullable(userEntity);
    }

    private String getRedisKey(String username) {
        return "user:" + username;
    }
}

package org.boot.registersession.repository;

import java.util.List;
import java.util.Optional;
import org.boot.registersession.model.entity.CrashSessionEntity;
import org.boot.registersession.model.entity.RegistrationEntity;
import org.boot.registersession.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationEntityRepository extends JpaRepository<RegistrationEntity, Long> {

    List<RegistrationEntity> findByUser(UserEntity user);

    Optional<RegistrationEntity> findByRegistrationIdAndUser(Long registrationId, UserEntity user);

    Optional<RegistrationEntity> findByUserAndSession(UserEntity user, CrashSessionEntity session);
}

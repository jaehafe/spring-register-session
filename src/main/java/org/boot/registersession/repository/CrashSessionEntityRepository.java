package org.boot.registersession.repository;

import org.boot.registersession.model.entity.CrashSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrashSessionEntityRepository extends JpaRepository<CrashSessionEntity, Long> {

}

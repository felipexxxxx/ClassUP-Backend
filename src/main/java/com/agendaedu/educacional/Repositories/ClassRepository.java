package com.agendaedu.educacional.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.agendaedu.educacional.Entities.ClassEntity;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByCodigoAcesso(String codigoAcesso);
}

package com.agendaedu.educacional.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.agendaedu.educacional.Entities.Class;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class, Long> {
    Optional<Class> findByCodigoAcesso(String codigoAcesso);
}

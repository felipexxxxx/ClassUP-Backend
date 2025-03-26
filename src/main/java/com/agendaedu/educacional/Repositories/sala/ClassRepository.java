package com.agendaedu.educacional.Repositories.sala;
import org.springframework.data.jpa.repository.JpaRepository;

import com.agendaedu.educacional.Entities.sala.*;
import com.agendaedu.educacional.Entities.usuario.*;

import java.util.List;
import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByCodigoAcesso(String codigoAcesso);
    boolean existsByNomeAndProfessor(String nome, User professor);
    List<ClassEntity> findByProfessor(User professor);
}

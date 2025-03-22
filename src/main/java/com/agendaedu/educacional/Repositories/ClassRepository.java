package com.agendaedu.educacional.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agendaedu.educacional.Entities.User;
import java.util.List;

import com.agendaedu.educacional.Entities.ClassEntity;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByCodigoAcesso(String codigoAcesso);
    boolean existsByNomeAndProfessor(String nome, User professor);
    List<ClassEntity> findByProfessor(User professor);
}

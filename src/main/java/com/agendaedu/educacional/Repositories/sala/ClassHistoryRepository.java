package com.agendaedu.educacional.Repositories.sala;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendaedu.educacional.Entities.sala.*;
import com.agendaedu.educacional.Entities.usuario.*;
import com.agendaedu.educacional.Enums.*;

import java.util.List;

public interface ClassHistoryRepository extends JpaRepository<ClassHistoryEntity, Long> {
    List<ClassHistoryEntity> findByUsuario(User usuario);
    boolean existsBySalaAndUsuarioAndRole(ClassEntity sala, User usuario, Role role);
    List<ClassHistoryEntity> findBySala(ClassEntity sala);
    List<ClassHistoryEntity> findBySalaAndUsuario(ClassEntity sala, User usuario);
}

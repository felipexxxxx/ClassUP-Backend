package com.agendaedu.educacional.Repositories.sala;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agendaedu.educacional.Entities.sala.ClassEntity;
import com.agendaedu.educacional.Entities.sala.ClassHistoryEntity;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Enums.Role;

import java.util.List;

public interface ClassHistoryRepository extends JpaRepository<ClassHistoryEntity, Long> {
    List<ClassHistoryEntity> findByUsuario(User usuario);
    boolean existsBySalaAndUsuarioAndRole(ClassEntity sala, User usuario, Role role);
    List<ClassHistoryEntity> findBySala(ClassEntity sala);
    List<ClassHistoryEntity> findBySalaAndUsuario(ClassEntity sala, User usuario);
}

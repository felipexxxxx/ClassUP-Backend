package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.enums.Role;
import com.agendaedu.educacional.Entities.ClassHistoryEntity;

import java.util.List;

public interface ClassHistoryRepository extends JpaRepository<ClassHistoryEntity, Long> {
    List<ClassHistoryEntity> findByUsuario(User usuario);
    boolean existsBySalaAndUsuarioAndRole(ClassEntity sala, User usuario, Role role);
    List<ClassHistoryEntity> findBySala(ClassEntity sala);
    List<ClassHistoryEntity> findBySalaAndUsuario(ClassEntity sala, User usuario);
}

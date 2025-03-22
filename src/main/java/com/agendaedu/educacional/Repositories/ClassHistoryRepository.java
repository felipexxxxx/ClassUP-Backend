package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.ClassHistoryEntity;
import com.agendaedu.educacional.Entities.Role;
import java.util.List;

public interface ClassHistoryRepository extends JpaRepository<ClassHistoryEntity, Long> {
    List<ClassHistoryEntity> findByUsuario(User usuario);
    boolean existsBySalaAndUsuarioAndRole(ClassEntity sala, User usuario, Role role);

}

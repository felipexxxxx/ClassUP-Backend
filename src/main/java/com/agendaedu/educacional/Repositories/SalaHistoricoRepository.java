package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Entities.HistoricClasses;
import org.springframework.data.jpa.repository.JpaRepository;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.Role;
import java.util.List;

public interface SalaHistoricoRepository extends JpaRepository<HistoricClasses, Long> {
    List<HistoricClasses> findByUsuario(User usuario);
    boolean existsBySalaAndUsuarioAndRole(ClassEntity sala, User usuario, Role role);

}

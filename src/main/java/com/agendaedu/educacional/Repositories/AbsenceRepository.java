package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.Absence;
import com.agendaedu.educacional.Entities.Activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    Optional<Absence> findByUsuarioAndAtividade(User usuario, Activity atividade);
}

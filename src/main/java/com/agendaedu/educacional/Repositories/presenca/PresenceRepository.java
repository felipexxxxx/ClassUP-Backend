package com.agendaedu.educacional.Repositories.presenca;

import com.agendaedu.educacional.Entities.atividade.*;
import com.agendaedu.educacional.Entities.presenca.*;
import com.agendaedu.educacional.Entities.usuario.*;
import com.agendaedu.educacional.Enums.*;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    Optional<Presence> findByUsuarioAndAtividade(User usuario, Activity atividade);
    int countByAtividadeAndStatus(Activity atividade, PresenceStatus status);
    List<Presence> findByUsuarioId(Long usuarioId);
    List<Presence> findByAtividadeSalaId(Long salaId);


}
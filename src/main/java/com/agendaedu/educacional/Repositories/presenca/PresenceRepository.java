package com.agendaedu.educacional.Repositories.presenca;

import com.agendaedu.educacional.Entities.atividade.Activity;
import com.agendaedu.educacional.Entities.presenca.Presence;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Enums.PresenceStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    Optional<Presence> findByUsuarioAndAtividade(User usuario, Activity atividade);
    int countByAtividadeAndStatus(Activity atividade, PresenceStatus status);
    List<Presence> findByUsuarioId(Long usuarioId);
    List<Presence> findByAtividadeSalaId(Long salaId);


}
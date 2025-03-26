package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.Presence;
import com.agendaedu.educacional.Entities.PresenceStatus;
import com.agendaedu.educacional.Entities.Activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PresenceRepository extends JpaRepository<Presence, Long> {
    Optional<Presence> findByUsuarioAndAtividade(User usuario, Activity atividade);
    int countByAtividadeAndStatus(Activity atividade, PresenceStatus status);

}

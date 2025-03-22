package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.Presence;
import com.agendaedu.educacional.Entities.Activity;
import com.agendaedu.educacional.Entities.PresenceStatus;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Repositories.PresenceRepository;
import com.agendaedu.educacional.Repositories.ActivityRepository;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public void confirmarPresenca(Long atividadeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Activity atividade = activityRepository.findById(atividadeId)
        .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Presence presence = presenceRepository.findByUsuarioAndAtividade(user, atividade)
        .orElseThrow(() -> new RuntimeException("Presença não registrada."));

    presence.setStatus(PresenceStatus.CONFIRMADO);
    presenceRepository.save(presence);
}


    @Transactional
    public void cancelarPresenca(Long atividadeId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Activity atividade = activityRepository.findById(atividadeId)
        .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Presence presence = presenceRepository.findByUsuarioAndAtividade(user, atividade)
        .orElseThrow(() -> new RuntimeException("Presença não registrada."));

    presence.setStatus(PresenceStatus.RECUSADO);
    presenceRepository.save(presence);
}
}
package com.agendaedu.educacional.Services.presenca;

import com.agendaedu.educacional.Entities.atividade.Activity;
import com.agendaedu.educacional.Entities.presenca.Presence;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Enums.PresenceStatus;
import com.agendaedu.educacional.Repositories.atividade.ActivityRepository;
import com.agendaedu.educacional.Repositories.presenca.PresenceRepository;

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

    presence.setStatus(PresenceStatus.CANCELADO);
    presenceRepository.save(presence);
}
}
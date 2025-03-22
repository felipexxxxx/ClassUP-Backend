package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.Presence;
import com.agendaedu.educacional.Entities.Activity;
import com.agendaedu.educacional.Entities.PresenceStatus;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Repositories.PresenceRepository;
import com.agendaedu.educacional.Repositories.ActivityRepository;
import com.agendaedu.educacional.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PresenceService {

    private final PresenceRepository absenceRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    public void confirmAttendance(Long userId, Long activityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
    
        Presence absence = absenceRepository.findByUsuarioAndAtividade(user, activity)
                .orElse(new Presence(null, PresenceStatus.PENDENTE, user, activity));
    
        absence.setStatus(PresenceStatus.CONFIRMADO);
        absenceRepository.save(absence);
    }

    @Transactional
    public void cancelAttendance(Long userId, Long activityId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Presence absence = absenceRepository.findByUsuarioAndAtividade(user, activity)
            .orElseThrow(() -> new RuntimeException("Presença não registrada."));

    absence.setStatus(PresenceStatus.RECUSADO);
    absenceRepository.save(absence);
    }
}

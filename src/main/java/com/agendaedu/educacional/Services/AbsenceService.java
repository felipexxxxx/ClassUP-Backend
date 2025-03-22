package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.Absence;
import com.agendaedu.educacional.Entities.Activity;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Repositories.AbsenceRepository;
import com.agendaedu.educacional.Repositories.ActivityRepository;
import com.agendaedu.educacional.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;

    @Transactional
    public void confirmAttendance(Long userId, Long activityId) {
        User usuario = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Activity atividade = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Optional<Absence> absence = absenceRepository.findByUsuarioAndAtividade(usuario, atividade);
        if (absence.isEmpty()) {
            Absence newAbsence = Absence.builder()
                    .usuario(usuario)
                    .atividade(atividade)
                    .presente(true)
                    .build();
            absenceRepository.save(newAbsence);
        } else {
            throw new RuntimeException("Presença já confirmada.");
        }
    }

    @Transactional
    public void cancelAttendance(Long userId, Long activityId) {
        User usuario = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Activity atividade = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        Absence absence = absenceRepository.findByUsuarioAndAtividade(usuario, atividade)
                .orElseThrow(() -> new RuntimeException("Presença não confirmada."));

        absenceRepository.delete(absence);
    }
}

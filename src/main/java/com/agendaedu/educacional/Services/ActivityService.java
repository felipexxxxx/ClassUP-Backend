package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.*;
import com.agendaedu.educacional.Repositories.ActivityRepository;
import com.agendaedu.educacional.Repositories.NotificationRepository;
import com.agendaedu.educacional.Repositories.UserRepository;
import com.agendaedu.educacional.Repositories.PresenceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final PresenceRepository presenceRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService; 

    @Transactional
    public Activity createActivity(Activity activity) {
        Activity savedActivity = activityRepository.save(activity);

    // Pega todos os alunos da sala
        List<User> alunos = userRepository.findBySalaAndRole(activity.getSala(), Role.ALUNO);

    for (User aluno : alunos) {
        // Cria a notificação
        Notification notification = Notification.builder()
                .atividade(savedActivity)
                .usuario(aluno)
                .mensagem("Nova atividade: " + savedActivity.getTitulo())
                .dataEnvio(savedActivity.getDataHora())
                .build();
        notificationRepository.save(notification);

        // Cria presença com status PENDENTE
        Presence presence = Presence.builder()
                .atividade(savedActivity)
                .usuario(aluno)
                .status(PresenceStatus.PENDENTE)
                .build();
        presenceRepository.save(presence);

        // Envia e-mail (opcional)
        String emailBody = """
            Olá %s,

            Uma nova atividade foi criada:

            Título: %s
            Descrição: %s
            Local: %s
            Data e Hora: %s

            Confirme sua presença no portal.

            Abraço,
            Agenda Edu
            """.formatted(aluno.getNomeCompleto(), activity.getTitulo(), activity.getDescricao(), activity.getLocal(), activity.getDataHora());

        emailService.sendEmail(aluno.getEmail(), "Nova Atividade: " + activity.getTitulo(), emailBody);
    }

    return savedActivity;
}


    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public List<Activity> getActivitiesBySala(Long salaId) {
        return activityRepository.findBySalaId(salaId);
    }

    public Activity getById(Long id) {
        return activityRepository.findById(id).orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
    }

    public List<Activity> getAll() {
        return activityRepository.findAll();
    }
    
}

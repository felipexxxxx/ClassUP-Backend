package com.agendaedu.educacional.Services.atividade;

import com.agendaedu.educacional.DTOs.atividade.ActivityDTO;
import com.agendaedu.educacional.DTOs.atividade.ActivityResumoDTO;
import com.agendaedu.educacional.Entities.atividade.Activity;
import com.agendaedu.educacional.Entities.presenca.Presence;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Enums.PresenceStatus;
import com.agendaedu.educacional.Enums.Role;
import com.agendaedu.educacional.Repositories.atividade.ActivityRepository;
import com.agendaedu.educacional.Repositories.presenca.PresenceRepository;
import com.agendaedu.educacional.Repositories.usuario.UserRepository;
import com.agendaedu.educacional.Services.usuario.EmailService;

import jakarta.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;  
import org.springframework.security.core.Authentication;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final PresenceRepository presenceRepository;
    private final UserRepository userRepository;
    private final EmailService emailService; 
   
    @Transactional
    public Activity criarAtividade(Activity activity) {
        Activity savedActivity = activityRepository.save(activity);
        List<User> alunos = userRepository.findBySalaAndRole(activity.getSala(), Role.ALUNO);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM 'às' HH:mm");
        String dataFormatada = activity.getDataHora().format(formatter);

        for (User aluno : alunos) {
            Presence presence = Presence.builder()
                    .atividade(savedActivity)
                    .usuario(aluno)
                    .status(PresenceStatus.PENDENTE)
                    .build();
            presenceRepository.save(presence);

            String emailBody = """
                Olá %s,

                Uma nova atividade foi criada:

                Título: %s

                Descrição: %s

                Local: %s
                Dia: %s

                Confirme sua presença no portal😊.

                Atenciosamente,
                ClassUP
                """.formatted(
                    aluno.getNomeCompleto(),
                    activity.getTitulo(),
                    activity.getDescricao(),
                    activity.getLocal(),
                    dataFormatada
            );

            emailService.sendEmail(aluno.getEmail(), "Nova Atividade: " + activity.getTitulo(), emailBody);
        }

        return savedActivity;
    }

    @Transactional
    public void editarAtividade(Long id, ActivityDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User professor = (User) auth.getPrincipal();

        Activity atividade = activityRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        // Verifica se o professor atual é o dono da sala vinculada à atividade
        if (!atividade.getSala().getProfessor().getId().equals(professor.getId())) {
            throw new RuntimeException("Você não tem permissão para editar esta atividade.");
        }

        atividade.setTitulo(dto.titulo());
        atividade.setDescricao(dto.descricao());
        atividade.setLocal(dto.local());
        atividade.setDataHora(dto.data()); 

        activityRepository.save(atividade);
    }

    @Transactional
    public void deletarAtividade(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User professor = (User) auth.getPrincipal();

        Activity atividade = activityRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));

        if (!atividade.getSala().getProfessor().getId().equals(professor.getId())) {
        throw new RuntimeException("Você não tem permissão para excluir esta atividade.");
        }

        activityRepository.delete(atividade);
}

public ActivityResumoDTO getResumoAtividade(Long atividadeId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User professor = (User) auth.getPrincipal();

    Activity atividade = activityRepository.findById(atividadeId)
        .orElseThrow(() -> new RuntimeException("Atividade não encontrada."));


    if (atividade.getSala().getProfessor() == null || 
        !atividade.getSala().getProfessor().getId().equals(professor.getId())) {
        throw new RuntimeException("Você não tem permissão para visualizar essa atividade.");
    }

    int confirmados = presenceRepository.countByAtividadeAndStatus(atividade, PresenceStatus.CONFIRMADO);
    
    return new ActivityResumoDTO(
        atividade.getId(),
        atividade.getTitulo(),
        atividade.getDescricao(),
        atividade.getDataHora(),
        atividade.getLocal(),
        confirmados
    );
}




}

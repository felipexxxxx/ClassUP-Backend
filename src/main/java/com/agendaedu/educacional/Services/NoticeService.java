package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.DTOs.NoticeDTO;

import com.agendaedu.educacional.Entities.*;
import com.agendaedu.educacional.Repositories.ClassRepository;
import com.agendaedu.educacional.Repositories.NoticeRepository;
import com.agendaedu.educacional.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ClassRepository classRepository;

    @Transactional
    public String createNotice(NoticeDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User professor = (User) auth.getPrincipal();
    
        if (!professor.getRole().equals(Role.PROFESSOR)) {
            throw new RuntimeException("Apenas professores podem enviar avisos.");
        }
    
        if (dto.getSala() == null || dto.getSala().getId() == null) {
            throw new RuntimeException("Sala não informada.");
        }
    
        ClassEntity sala = classRepository.findById(dto.getSala().getId())
            .orElseThrow(() -> new RuntimeException("Sala não encontrada."));
    
        if (!sala.getProfessor().getId().equals(professor.getId())) {
            throw new RuntimeException("Você não tem permissão para enviar aviso para essa sala.");
        }
    
        Notice aviso = Notice.builder()
            .titulo(dto.getTitulo())
            .mensagem(dto.getMensagem())
            .enviadaEm(LocalDateTime.now())
            .sala(sala)
            .build();
    
        noticeRepository.save(aviso);
    
        List<User> alunos = userRepository.findBySalaAndRole(sala, Role.ALUNO);
        for (User aluno : alunos) {
            String emailBody = """
                Olá %s,
    
                Um novo aviso foi enviado para sua sala:
    
                Título: %s
                %s
                
                Acesse a plataforma para mais detalhes.
    
                Atenciosamente,
                ClassUP
            """.formatted(aluno.getNomeCompleto(), aviso.getTitulo(), aviso.getMensagem());
    
            emailService.sendEmail(aluno.getEmail(), "Novo Aviso: " + aviso.getTitulo(), emailBody);
        }
    
        return "Aviso enviado com sucesso para os alunos da sala.";
    }
    



    public List<Notice> getNoticesByUserClass() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        ClassEntity classroom = user.getSala();
        if (classroom == null) {
            throw new RuntimeException("You are not assigned to any class.");
        }

        return noticeRepository.findBySala(classroom);
    }
}

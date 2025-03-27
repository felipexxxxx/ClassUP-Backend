package com.agendaedu.educacional.Services.aviso;

import com.agendaedu.educacional.DTOs.aviso.*;
import com.agendaedu.educacional.Entities.aviso.*;
import com.agendaedu.educacional.Entities.sala.*;
import com.agendaedu.educacional.Entities.usuario.*;
import com.agendaedu.educacional.Enums.*;
import com.agendaedu.educacional.Repositories.aviso.*;
import com.agendaedu.educacional.Repositories.sala.*;
import com.agendaedu.educacional.Repositories.usuario.*;
import com.agendaedu.educacional.Services.usuario.EmailService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ClassRepository classRepository;

    @Transactional
    public String criarAviso(NoticeDTO dto) {
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
            .enviadaEm(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
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
    
    @Transactional
    public void editarAviso(Long id, Notice avisoAtualizado) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User professor = (User) auth.getPrincipal();

        Notice avisoExistente = noticeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aviso não encontrado"));

        if (!avisoExistente.getSala().getProfessor().getId().equals(professor.getId())) {
            throw new RuntimeException("Você não tem permissão para editar este aviso.");
        }

        avisoExistente.setTitulo(avisoAtualizado.getTitulo());
        avisoExistente.setMensagem(avisoAtualizado.getMensagem());

        noticeRepository.save(avisoExistente);
    }

    @Transactional
    public void excluirAviso(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User professor = (User) auth.getPrincipal();

        Notice aviso = noticeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aviso não encontrado"));

        if (!aviso.getSala().getProfessor().getId().equals(professor.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir este aviso.");
        }

        noticeRepository.delete(aviso);
    }

    
}
    

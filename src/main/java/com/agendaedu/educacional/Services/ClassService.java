package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.Activity;
import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Entities.Role;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.ClassHistoryEntity;
import com.agendaedu.educacional.Entities.Presence;
import com.agendaedu.educacional.Entities.PresenceStatus;
import com.agendaedu.educacional.Repositories.ClassRepository;
import com.agendaedu.educacional.Repositories.UserRepository;
import com.agendaedu.educacional.Repositories.ClassHistoryRepository;
import com.agendaedu.educacional.Repositories.PresenceRepository;
import com.agendaedu.educacional.Repositories.ActivityRepository;
import com.agendaedu.educacional.Repositories.NoticeRepository;
import com.agendaedu.educacional.DTOs.ActivityHistoryDTO;
import com.agendaedu.educacional.DTOs.ClassHistoryDetalhesDTO;
import com.agendaedu.educacional.DTOs.GetClassDTO;
import com.agendaedu.educacional.DTOs.NoticeHistoryDTO;
import com.agendaedu.educacional.DTOs.SimpleUserDTO;
import com.agendaedu.educacional.DTOs.StudentClassDTO;
import java.util.List;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ClassHistoryRepository salaHistoricoRepository;
    private final PresenceRepository presenceRepository;
    private final ActivityRepository activityRepository;
    private final NoticeRepository noticeRepository;

    @Transactional
    public ClassEntity createClass(ClassEntity classEntity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!user.getRole().equals(Role.PROFESSOR)) {
            throw new RuntimeException("Apenas professores podem criar salas.");
        }

        boolean salaExiste = classRepository.existsByNomeAndProfessor(classEntity.getNome(), user);
        if (salaExiste) {
            throw new RuntimeException("Você já criou uma sala com esse nome.");
        }

        String codigo = "SALA-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        classEntity.setProfessor(user);
        classEntity.setCodigoAcesso(codigo);
        return classRepository.save(classEntity);
}

    @Transactional
public String joinClass(String codigoDeEntrada) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) auth.getPrincipal();

    ClassEntity classEntity = classRepository.findByCodigoAcesso(codigoDeEntrada)
            .orElseThrow(() -> new RuntimeException("Código de sala inválido"));

    if (user.getSala() != null && user.getSala().getId().equals(classEntity.getId())) {
        throw new RuntimeException("Você já está nessa sala.");
    }

    if (user.getSala() != null) {
        throw new RuntimeException("Você já está em uma sala. Só é permitido ingressar em uma.");
    }

    // Associa aluno à sala
    user.setSala(classEntity);
    userRepository.save(user);

    List<Activity> atividades = activityRepository.findBySalaId(classEntity.getId());
        for (Activity atividade : atividades) {
            boolean jaExiste = presenceRepository.findByUsuarioAndAtividade(user, atividade).isPresent();
        if (!jaExiste) {
            Presence novaPresenca = new Presence();
            novaPresenca.setUsuario(user);
            novaPresenca.setAtividade(atividade);
            novaPresenca.setStatus(PresenceStatus.PENDENTE);
            presenceRepository.save(novaPresenca);
        }
    }


    return "Usuário adicionado à sala com sucesso.";
}

    @Transactional
    public String encerrarSemestre() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User professor = (User) auth.getPrincipal();

        if (!professor.getRole().equals(Role.PROFESSOR)) {
        throw new RuntimeException("Apenas professores podem encerrar o semestre.");
    }

    // Busca apenas salas onde ele ainda é o professor vinculado
        List<ClassEntity> salasAtivas = classRepository.findByProfessor(professor);

        if (salasAtivas.isEmpty()) {
            throw new RuntimeException("Você não possui salas ativas para encerrar.");
        }

        for (ClassEntity sala : salasAtivas) {
            // Se a sala já estiver no histórico, a gente não ignora — a gente verifica se AINDA está ativo!
            // Isso garante que professor só será desvinculado se ainda está vinculado
            if (sala.getProfessor() != null && sala.getProfessor().getId().equals(professor.getId())) {

                // 1. Registra professor no histórico
                if (!salaHistoricoRepository.existsBySalaAndUsuarioAndRole(sala, professor, Role.PROFESSOR)) {
                    salaHistoricoRepository.save(ClassHistoryEntity.builder()
                            .usuario(professor)
                            .sala(sala)
                            .role(Role.PROFESSOR)
                            .dataEncerramento(LocalDateTime.now())
                            .build());
            }

                // 2. Registra alunos e desvincula
                List<User> alunos = userRepository.findBySalaAndRole(sala, Role.ALUNO);
                for (User aluno : alunos) {
                    if (!salaHistoricoRepository.existsBySalaAndUsuarioAndRole(sala, aluno, Role.ALUNO)) {
                        salaHistoricoRepository.save(ClassHistoryEntity.builder()
                                .usuario(aluno)
                                .sala(sala)
                                .role(Role.ALUNO)
                                .dataEncerramento(LocalDateTime.now())
                                .build());
                    }

                    aluno.setSala(null);
                }

                // 3. Desvincula o professor da sala
                sala.setProfessor(null);

                // 4. Salva atualizações
                userRepository.saveAll(alunos);
                classRepository.save(sala);
            }
        }

    return "Semestre encerrado com sucesso. Todas as salas foram encerradas.";
}

   public ClassHistoryDetalhesDTO buscarDetalhesHistorico(Long salaId) {
    ClassEntity sala = classRepository.findById(salaId)
        .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

    List<ClassHistoryEntity> historico = salaHistoricoRepository.findBySala(sala);

    // Professor
    User professor = historico.stream()
        .filter(h -> h.getRole() == Role.PROFESSOR)
        .map(ClassHistoryEntity::getUsuario)
        .findFirst()
        .orElse(null);

    // Alunos
    List<SimpleUserDTO> alunos = historico.stream()
        .filter(h -> h.getRole() == Role.ALUNO)
        .map(h -> new SimpleUserDTO(h.getUsuario().getId(), h.getUsuario().getNomeCompleto()))
        .toList();

    // Atividades da sala
    List<ActivityHistoryDTO> atividades = activityRepository.findBySalaId(salaId).stream()
        .map(a -> new ActivityHistoryDTO(a.getId(), a.getTitulo(), a.getDescricao(), a.getDataHora()))
        .toList();

    // Avisos da sala
    List<NoticeHistoryDTO> avisos = noticeRepository.findBySala(sala).stream()
    .map(n -> new NoticeHistoryDTO(n.getId(), n.getTitulo(), n.getMensagem(), n.getEnviadaEm()))
    .toList();


    return new ClassHistoryDetalhesDTO(
        sala.getNome(),
        sala.getCodigoAcesso(),
        historico.get(0).getDataEncerramento(),
        new SimpleUserDTO(professor.getId(), professor.getNomeCompleto()),
        alunos,
        atividades,
        avisos
    );
}



    public List<ClassHistoryEntity> listarHistoricoUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

            return salaHistoricoRepository.findByUsuario(user);
}

    @Transactional
    public String removerAlunoDaSala(Long alunoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User professor = (User) auth.getPrincipal();

    // Valida se é professor
        if (!professor.getRole().equals(Role.PROFESSOR)) {
            throw new RuntimeException("Apenas professores podem remover alunos.");
        }

        // Busca o aluno
        User aluno = userRepository.findById(alunoId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        // Valida se o aluno está em uma sala
        if (aluno.getSala() == null) {
            throw new RuntimeException("Este aluno não está em nenhuma sala.");
        }

        // Verifica se o professor é o dono da sala
        if (!aluno.getSala().getProfessor().getId().equals(professor.getId())) {
            throw new RuntimeException("Você não é o professor responsável por esta sala.");
        }

        // Remove aluno da sala
        aluno.setSala(null);
        userRepository.save(aluno);

        return "Aluno removido da sala com sucesso.";
}


    public StudentClassDTO getMinhaSalaAtual() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getRole().equals(Role.ALUNO)) {
            throw new RuntimeException("Apenas alunos possuem sala associada para esta consulta.");
        }

        ClassEntity sala = user.getSala();

        if (sala == null) {
            throw new RuntimeException("Você ainda não está vinculado a nenhuma sala.");
        }

        return new StudentClassDTO(
            sala.getId(),
            sala.getNome(),
            sala.getCodigoAcesso()
        );
    }


    public GetClassDTO detalharSalaDoAluno() {
        User aluno = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Recarrega o aluno completo do banco com a sala associada
        aluno = userRepository.findById(aluno.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ClassEntity sala = aluno.getSala();

        if (sala == null) {
            throw new RuntimeException("Você ainda não está vinculado a uma sala.");
        }

        // Busca a sala com alunos e professor (usando EntityGraph ou fetch join se quiser)
        sala = classRepository.findById(sala.getId())
                .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        SimpleUserDTO profDTO = new SimpleUserDTO(
            sala.getProfessor().getId(),
            sala.getProfessor().getNomeCompleto()
        );

        List<SimpleUserDTO> alunos = sala.getAlunos().stream()
            .map(a -> new SimpleUserDTO(a.getId(), a.getNomeCompleto()))
            .toList();

        return new GetClassDTO(
            sala.getId(),
            sala.getNome(),
            profDTO,
            alunos
        );
    }



   

}

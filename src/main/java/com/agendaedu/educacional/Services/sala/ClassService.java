package com.agendaedu.educacional.Services.sala;

import com.agendaedu.educacional.Entities.atividade.Activity;
import com.agendaedu.educacional.Entities.aviso.Notice;
import com.agendaedu.educacional.Entities.presenca.Presence;
import com.agendaedu.educacional.Entities.sala.ClassEntity;
import com.agendaedu.educacional.Entities.sala.ClassHistoryEntity;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Enums.PresenceStatus;
import com.agendaedu.educacional.Enums.Role;
import com.agendaedu.educacional.Repositories.atividade.ActivityRepository;
import com.agendaedu.educacional.Repositories.aviso.NoticeRepository;
import com.agendaedu.educacional.Repositories.presenca.PresenceRepository;
import com.agendaedu.educacional.Repositories.sala.ClassHistoryRepository;
import com.agendaedu.educacional.Repositories.sala.ClassRepository;
import com.agendaedu.educacional.Repositories.usuario.UserRepository;
import com.agendaedu.educacional.Services.usuario.EmailService;
import com.agendaedu.educacional.DTOs.atividade.ActivityDTO;
import com.agendaedu.educacional.DTOs.atividade.ActivityHistoryDTO;
import com.agendaedu.educacional.DTOs.aviso.NoticeExibicaoDTO;
import com.agendaedu.educacional.DTOs.sala.ClassDTO;
import com.agendaedu.educacional.DTOs.sala.ClassHistoryDetalhesDTO;
import com.agendaedu.educacional.DTOs.sala.GetClassDTO;
import com.agendaedu.educacional.DTOs.sala.GetClassDetalhadoDTO;
import com.agendaedu.educacional.DTOs.usuario.SimpleUserDTO;

import java.util.List;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Random;
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
    private final EmailService emailService;

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

    // Busca todas as salas ativas vinculadas a esse professor
    List<ClassEntity> salasAtivas = classRepository.findByProfessor(professor);

    if (salasAtivas.isEmpty()) {
        throw new RuntimeException("Você não possui salas ativas para encerrar.");
    }

    for (ClassEntity sala : salasAtivas) {
        // Confirma que o professor ainda está vinculado
        if (sala.getProfessor() != null && sala.getProfessor().getId().equals(professor.getId())) {

            // 1. Salva o professor no histórico (caso ainda não esteja)
            if (!salaHistoricoRepository.existsBySalaAndUsuarioAndRole(sala, professor, Role.PROFESSOR)) {
                salaHistoricoRepository.save(ClassHistoryEntity.builder()
                        .usuario(professor)
                        .sala(sala)
                        .role(Role.PROFESSOR)
                        .dataEncerramento(LocalDateTime.now())
                        .build());
            }

            // 2. Salva os alunos no histórico e os desvincula da sala
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
                

                // 3. Envia e-mail ao aluno
                String corpoEmail = """
                    Olá %s,

                    Informamos que o semestre da sala "%s" foi encerrado pelo professor(a) %s.

                    Você foi desvinculado desta sala e poderá ingressar em uma nova assim que disponível.
                    Boas férias😊😊!

                    Atenciosamente,
                    ClassUP
                    """.formatted(
                        aluno.getNomeCompleto(),
                        sala.getNome(),
                        professor.getNomeCompleto()
                );

                emailService.sendEmail(
                    aluno.getEmail(),
                    "Semestre Encerrado - Sala " + sala.getNome(),
                    corpoEmail
                );
            }

            // 4. Desvincula o professor da sala
            sala.setProfessor(null);
            sala.setCodigoAcesso(CodigoAcessoUtil.gerarCodigoAcesso());
            // 5. Salva atualizações no banco
            userRepository.saveAll(alunos);
            classRepository.save(sala);
        }
    }

    return "Semestre encerrado com sucesso. Todas as salas foram encerradas.";
}


public ClassHistoryDetalhesDTO buscarDetalhesHistorico(Long salaId) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User usuarioLogado = (User) auth.getPrincipal();

    ClassEntity sala = classRepository.findById(salaId)
        .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

        List<ClassHistoryEntity> historico = salaHistoricoRepository.findBySalaAndUsuario(sala, usuarioLogado);

    if (historico.isEmpty()) {
        throw new RuntimeException("Você não participou dessa sala.");
    }

    // Professor
    User professor = salaHistoricoRepository.findBySala(sala).stream()
        .filter(h -> h.getRole() == Role.PROFESSOR)
        .map(ClassHistoryEntity::getUsuario)
        .findFirst()
        .orElse(null);

    // Alunos
    List<SimpleUserDTO> alunos = salaHistoricoRepository.findBySala(sala).stream()
        .filter(h -> h.getRole() == Role.ALUNO)
        .map(h -> new SimpleUserDTO(h.getUsuario().getId(), h.getUsuario().getNomeCompleto()))
        .toList();

    // Atividades da sala (com status real do aluno logado)
    List<Activity> atividadesSala = activityRepository.findBySalaId(salaId);
    List<ActivityHistoryDTO> atividades = atividadesSala.stream()
        .map(a -> {
            PresenceStatus status = presenceRepository
                .findByUsuarioAndAtividade(usuarioLogado, a)
                .map(Presence::getStatus)
                .orElse(PresenceStatus.PENDENTE);

            return new ActivityHistoryDTO(
                a.getId(),
                a.getTitulo(),
                a.getDescricao(),
                a.getDataHora(),
                status,
                a.getLocal()
            );
        })
        .toList();

    List<NoticeExibicaoDTO> avisos = noticeRepository.findBySala(sala).stream()
        .map(n -> new NoticeExibicaoDTO(n.getId(), n.getTitulo(), n.getMensagem(), n.getEnviadaEm()))
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

    String nomeSala = aluno.getSala().getNome();

    // Remove aluno da sala
    aluno.setSala(null);
    userRepository.save(aluno);

    // Envia e-mail ao aluno
    String assunto = "Remoção da sala";
    String mensagem = """
        Olá %s,

        Informamos que você foi removido da sala "%s" pelo professor %s.

        Caso tenha dúvidas, entre em contato com o professor.

        Atenciosamente,
        ClassUP
        """.formatted(aluno.getNomeCompleto(), nomeSala, professor.getNomeCompleto());

    emailService.sendEmail(aluno.getEmail(), assunto, mensagem);

    return "Aluno removido da sala com sucesso.";
}



    public ClassDTO getMinhaSalaAtual() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!user.getRole().equals(Role.ALUNO)) {
            throw new RuntimeException("Apenas alunos possuem sala associada para esta consulta.");
        }

        ClassEntity sala = user.getSala();

        if (sala == null) {
            throw new RuntimeException("Você ainda não está vinculado a nenhuma sala.");
        }

        return new ClassDTO(
            sala.getId(),
            sala.getNome(),
            sala.getCodigoAcesso()
        );
    }


    public GetClassDTO detalharSalaDoAluno() {
        User aluno = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        aluno = userRepository.findById(aluno.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        ClassEntity sala = aluno.getSala();

        if (sala == null) {
            throw new RuntimeException("Você ainda não está vinculado a uma sala.");
        }

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

    public List<ClassDTO> getSalasDoProfessor() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User professor = (User) auth.getPrincipal();

    if (!professor.getRole().equals(Role.PROFESSOR)) {
        throw new RuntimeException("Apenas professores podem visualizar suas salas.");
    }

    return classRepository.findByProfessor(professor).stream()
            .map(sala -> new ClassDTO(
                sala.getId(),
                sala.getNome(),
                sala.getCodigoAcesso()
            ))
            .toList();
}


public GetClassDetalhadoDTO getDetalhesSalaPorId(Long salaId) {
    ClassEntity sala = classRepository.findById(salaId)
        .orElseThrow(() -> new RuntimeException("Sala não encontrada"));

    List<User> alunos = userRepository.findBySalaAndRole(sala, Role.ALUNO);
    List<Notice> avisos = noticeRepository.findBySala(sala);

    List<ActivityDTO> atividadesDTO = activityRepository.findBySalaId(salaId)
        .stream()
        .map(a -> new ActivityDTO(
            a.getId(),
            a.getTitulo(),
            a.getDescricao(),
            a.getLocal(),
            a.getDataHora() 
        ))
        .toList();

    return new GetClassDetalhadoDTO(
        sala.getId(),
        sala.getNome(),
        sala.getCodigoAcesso(),
        sala.getProfessor(),
        alunos,
        atividadesDTO,
        avisos
    );
}


public class CodigoAcessoUtil {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TAMANHO = 10;

    public static String gerarCodigoAcesso() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(TAMANHO);
        for (int i = 0; i < TAMANHO; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}




   

}

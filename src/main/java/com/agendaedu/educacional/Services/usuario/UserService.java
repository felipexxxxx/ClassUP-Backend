package com.agendaedu.educacional.Services.usuario;

import com.agendaedu.educacional.DTOs.autenticacao.*;
import com.agendaedu.educacional.DTOs.usuario.*;
import com.agendaedu.educacional.Entities.usuario.*;
import com.agendaedu.educacional.Exceptions.*;
import com.agendaedu.educacional.Repositories.usuario.*;
import com.agendaedu.educacional.Services.autenticacao.TokenService;
import com.agendaedu.educacional.Services.sala.ClassService.CodigoAcessoUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    public NewUserDTO registrar(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("e-mail", user.getEmail());
        }
    
        if (userRepository.findByCpf(user.getCpf()).isPresent()) {
            throw new UserAlreadyExistsException("CPF", user.getCpf());
        }
    
        if (userRepository.findByMatricula(user.getMatricula()).isPresent()) {
            throw new UserAlreadyExistsException("matrícula", user.getMatricula());
        }
    
        String senhaOriginal = user.getSenha(); // Salva a senha antes
    
        
        user.setSenha(passwordEncoder.encode(senhaOriginal));
    
        User novoUsuario = userRepository.save(user);
    
        return new NewUserDTO(
            novoUsuario.getEmail(),
            novoUsuario.getMatricula(),
            senhaOriginal,
            "Usuário criado com sucesso!"
        );
    }
    

    public LoginResponseDTO login(LoginRequestDTO dto) {
    Optional<User> optionalUser = Optional.empty();

    if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
        optionalUser = userRepository.findByEmail(dto.getEmail());
    } else if (dto.getMatricula() != null && !dto.getMatricula().isEmpty()) {
        optionalUser = userRepository.findByMatricula(dto.getMatricula());
    } else {
        throw new RuntimeException("Informe o e-mail ou a matrícula para login.");
    }

    User user = optionalUser.orElseThrow(() ->
        new UserNotFoundException(dto.getEmail() != null ? dto.getEmail() : dto.getMatricula())
    );

    if (!passwordEncoder.matches(dto.getSenha(), user.getSenha())) {
        throw new InvalidCredentialsException();
    }

    String token = tokenService.generateToken(user);

    // Cria uma sessão (vai p banco) 
    UserSession session = UserSession.builder()
        .user(user)
        .entrou(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
        .build();

    sessionRepository.save(session);

    return new LoginResponseDTO("Login realizado com sucesso!", token);
}
        @Transactional
        public void logout() {
            User user = getUsuarioLogado();

            Optional<UserSession> optionalSession = sessionRepository
                    .findTopByUserOrderByEntrouDesc(user);

            if (optionalSession.isPresent()) {
                UserSession session = optionalSession.get();
                session.setSaiu(LocalDateTime.now());
                sessionRepository.save(session);
            }
        }

     public UserInfoDTO getPerfil() {
        User user = getUsuarioLogado();
        return new UserInfoDTO(user.getNomeCompleto(), user.getEmail(), user.getMatricula());
    }

    public String atualizarEmail(GetNovoEmailDTO dto) {
        User user = getUsuarioLogado();
        user.setEmail(dto.novoEmail());
        userRepository.save(user);
        return "Email atualizado com sucesso!";
    }

    public String atualizarSenha(UpdatePasswordDTO dto) {
        User user = getUsuarioLogado();

        if (!passwordEncoder.matches(dto.senhaAtual(), user.getSenha())) {
            throw new RuntimeException("Senha atual incorreta.");
        }

        user.setSenha(passwordEncoder.encode(dto.novaSenha()));
        userRepository.save(user);
        return "Senha atualizada com sucesso!";
    }

        private User getUsuarioLogado() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return (User) auth.getPrincipal();
    }

    public ResponseEntity<String> mandarEmail(SendOldEmailDTO dto) {
    Optional<User> userOpt = userRepository.findByEmail(dto.email());

    if (userOpt.isEmpty()) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body("E-mail não encontrado.");
    }

    User user = userOpt.get();
    String codigo = CodigoAcessoUtil.gerarCodigoAcesso();
    user.setCodigoAutenticacao(codigo);
    userRepository.save(user);

    String dataAtual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));

    String emailBody = """
            Olá %s,

            Recebemos uma solicitação para redefinir sua senha no ClassUP.

            Aqui está seu código de autenticação:

            ➡️ **%s**

            Solicitação feita em: %s

            Se você não solicitou essa alteração, por favor, ignore este e-mail.

            Atenciosamente,  
            ClassUP
            """.formatted(
                user.getNomeCompleto(),
                codigo,
                dataAtual
    );

    emailService.sendEmail(user.getEmail(), "Redefinição de Senha - ClassUP", emailBody);

    return ResponseEntity.ok("Código de autenticação enviado para o e-mail.");
}


    
    public ResponseEntity<String> redefinirSenhaComCodigo(UpdatePasswordCodeDTO dto) {
        Optional<User> userOpt = userRepository.findByCodigoAutenticacao(dto.codigoAutenticacao());

        if (userOpt.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body("Código inválido ou expirado.");
        }

        User user = userOpt.get();
        user.setSenha(passwordEncoder.encode(dto.novaSenha()));
        user.setCodigoAutenticacao(null);
        userRepository.save(user);

        return ResponseEntity.ok("Senha redefinida com sucesso!");
}

    
    


    
}


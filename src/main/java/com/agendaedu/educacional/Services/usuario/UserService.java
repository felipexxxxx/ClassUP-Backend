package com.agendaedu.educacional.Services.usuario;

import com.agendaedu.educacional.DTOs.autenticação.LoginRequestDTO;
import com.agendaedu.educacional.DTOs.autenticação.LoginResponseDTO;
import com.agendaedu.educacional.DTOs.usuario.NewUserDTO;
import com.agendaedu.educacional.DTOs.usuario.UpdateEmailDTO;
import com.agendaedu.educacional.DTOs.usuario.UpdatePasswordDTO;
import com.agendaedu.educacional.DTOs.usuario.UserInfoDTO;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Entities.usuario.UserSession;
import com.agendaedu.educacional.Exceptions.*;
import com.agendaedu.educacional.Repositories.usuario.SessionRepository;
import com.agendaedu.educacional.Repositories.usuario.UserRepository;
import com.agendaedu.educacional.Services.autenticação.TokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

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

    // Gera o token JWT
    String token = tokenService.generateToken(user);

    // Cria uma nova sessão 
    UserSession session = UserSession.builder()
        .user(user)
        .entrou(LocalDateTime.now())
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

    public String atualizarEmail(UpdateEmailDTO dto) {
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
}


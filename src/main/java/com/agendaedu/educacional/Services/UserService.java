package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.DTOs.LoginRequestDTO;
import com.agendaedu.educacional.DTOs.LoginResponseDTO;
import com.agendaedu.educacional.DTOs.NewUserDTO;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.UserSession;
import com.agendaedu.educacional.Exceptions.*;
import com.agendaedu.educacional.Repositories.UserRepository;
import com.agendaedu.educacional.Repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * Busca usuário pelo email.
     */
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Registra um novo usuário, garantindo que não haja duplicações.
     */
    public NewUserDTO registrar(User user) {
        // Verifica se o email já está cadastrado
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("e-mail", user.getEmail());
        }
    
        // Verifica se o CPF já está cadastrado
        if (userRepository.findByCpf(user.getCpf()).isPresent()) {
            throw new UserAlreadyExistsException("CPF", user.getCpf());
        }
    
        // Verifica se a matrícula já está cadastrada
        if (userRepository.findByMatricula(user.getMatricula()).isPresent()) {
            throw new UserAlreadyExistsException("matrícula", user.getMatricula());
        }
    
        String senhaOriginal = user.getSenha(); // Salva a senha antes de codificar (caso deseje retornar)
    
        // Codifica a senha antes de salvar no banco
        user.setSenha(passwordEncoder.encode(senhaOriginal));
    
        // Salva o usuário no banco de dados
        User novoUsuario = userRepository.save(user);
    
        // Retorna DTO com os dados do usuário criado
        return new NewUserDTO(
            novoUsuario.getEmail(),
            novoUsuario.getMatricula(),
            senhaOriginal,
            "Usuário criado com sucesso!"
        );
    }
    

    /**
     * Faz login e retorna um token de autenticação.
     */
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
        UserSession session = new UserSession(user, token);
        sessionRepository.save(session);
    
        return new LoginResponseDTO("Login realizado com sucesso!", token);
    }
}

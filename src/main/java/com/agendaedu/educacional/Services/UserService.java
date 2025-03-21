package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.DTOs.LoginRequestDTO;
import com.agendaedu.educacional.DTOs.LoginResponseDTO;
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
    public LoginResponseDTO registrar(User user) {
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
    
        // Codifica a senha antes de salvar no banco
        user.setSenha(passwordEncoder.encode(user.getSenha()));
    
        // Salva o usuário no banco de dados
        User novoUsuario = userRepository.save(user);
    
        // Gera um token JWT para o usuário recém-criado
        String token = tokenService.generateToken(novoUsuario);
    
        // Define a role do usuário em formato legível
        String roleFormatada = novoUsuario.getRole().name().equals("PROFESSOR") ? "Professor" : "Aluno";
    
        // Retorna um DTO contendo a mensagem de sucesso, o token gerado e a role formatada
        return new LoginResponseDTO(roleFormatada + " criado com sucesso!", token, roleFormatada);
    }

    /**
     * Faz login e retorna um token de autenticação.
     */
    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(dto.getEmail()));

        if (!passwordEncoder.matches(dto.getSenha(), user.getSenha())) {
            throw new InvalidCredentialsException();
        }

        String token = tokenService.generateToken(user);
        UserSession session = new UserSession(user, token);
        sessionRepository.save(session);

        return new LoginResponseDTO("Login realizado com sucesso!", token, user.getRole().name());
    }
}

package com.agendaedu.educacional.Config;


import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Repositories.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user;

        if (login.contains("@")) {
            // Login por email
            user = userRepository.findByEmail(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + login));
        } else {
            // Login por matrícula
            user = userRepository.findByMatricula(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com matrícula: " + login));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // mesmo que tenha logado por matrícula, o username será o email
                .password(user.getSenha())
                .roles(user.getRole().name())
                .build();
    }
}

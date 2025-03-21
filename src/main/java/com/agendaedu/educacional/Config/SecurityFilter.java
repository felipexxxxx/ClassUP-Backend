package com.agendaedu.educacional.Config;

import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.UserSession;
import com.agendaedu.educacional.Repositories.SessionRepository;
import com.agendaedu.educacional.Repositories.UserRepository;
import com.agendaedu.educacional.Services.TokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {
            String login = tokenService.validateToken(token);

            if (login != null) {
                Optional<User> optionalUser = login.contains("@")
                        ? userRepository.findByEmail(login)
                        : userRepository.findByMatricula(login);

                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    Optional<UserSession> sessionOpt = sessionRepository.findByToken(token);
                    if (sessionOpt.isPresent()) {
                        UserSession session = sessionOpt.get();
                        long minutosInativo = Duration.between(session.getLastActivity(), LocalDateTime.now()).toMinutes();

                        if (minutosInativo > 60) {
                            sessionRepository.delete(session);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return;
                        }

                        // Atualiza a sessão com nova data de atividade
                        session.setLastActivity(LocalDateTime.now());
                        sessionRepository.save(session);

                        var authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
                        var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.substring(7);
    }
}

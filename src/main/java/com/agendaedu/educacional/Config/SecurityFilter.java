package com.agendaedu.educacional.Config;

import com.agendaedu.educacional.Entities.User;
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
import java.util.Collections;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {
            String login = tokenService.validateToken(token);
            if (login != null) {
                Optional<User> optionalUser = userRepository.findByEmail(login);
                
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
                    var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    System.out.println("Usuário autenticado: " + user.getEmail() + " com Role: " + user.getRole());
                } else {
                    System.out.println("Usuário não encontrado no banco!");
                }
            } else {
                System.out.println("Token inválido ou expirado!");
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


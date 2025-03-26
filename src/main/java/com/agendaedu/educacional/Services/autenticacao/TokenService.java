package com.agendaedu.educacional.Services.autenticacao;

import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Repositories.usuario.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;

    @Value("${API_SECURITY_TOKEN_SECRET}")
    private String secret;

    @Value("${API_SECURITY_ACCESS_TOKEN_EXPIRATION_MS:86400000}") // default: 1 dia
    private long expiration;

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer("AgendaEdu")
                .withSubject(user.getEmail() != null ? user.getEmail() : user.getMatricula())
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret));
    }

    public User validateTokenAndGetUser(String token) {
        try {
            String login = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("AgendaEdu")
                    .build()
                    .verify(token)
                    .getSubject();

            Optional<User> optionalUser = userRepository.findByEmail(login);
            if (optionalUser.isEmpty()) {
                optionalUser = userRepository.findByMatricula(login);
            }

            return optionalUser.orElse(null);

        } catch (Exception e) {
            return null;
        }
    }
}

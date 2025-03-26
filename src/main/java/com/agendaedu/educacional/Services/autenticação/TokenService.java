package com.agendaedu.educacional.Services.autenticação;

import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Repositories.usuario.UserRepository;

import java.util.Optional;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UserRepository userRepository;

    private static final Dotenv dotenv = Dotenv.load();
    private final String SECRET = dotenv.get("API_SECURITY_TOKEN_SECRET");
    private final long ACCESS_TOKEN_EXPIRATION = Long.parseLong(
        dotenv.get("API_SECURITY_ACCESS_TOKEN_EXPIRATION_MS")
    );

    public String generateToken(User user) {
        return JWT.create()
                .withIssuer("AgendaEdu")
                .withSubject(user.getEmail()) // ou matrícula se preferir
                .withSubject(user.getMatricula())
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public User validateTokenAndGetUser(String token) {
        try {
            String login = JWT.require(Algorithm.HMAC256(SECRET))
                    .withIssuer("AgendaEdu")
                    .build()
                    .verify(token)
                    .getSubject();

            // Busca por email ou matrícula
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

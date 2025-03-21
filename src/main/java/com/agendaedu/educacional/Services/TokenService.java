package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    private static final Dotenv dotenv = Dotenv.load();

    private final String SECRET = dotenv.get("API_SECURITY_TOKEN_SECRET");
    private final long ACCESS_TOKEN_EXPIRATION = Long.parseLong(
        dotenv.get("API_SECURITY_ACCESS_TOKEN_EXPIRATION_MS")
    );

    /**
     * Gera um Access Token com validade de 24h.
     */
    public String generateToken(User user) {
        return JWT.create()
                .withIssuer("AgendaEdu")
                .withSubject(user.getEmail()) // ou .withSubject(user.getMatricula()) se preferir
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * Valida o token e retorna o "login" (email ou matrícula).
     */
    public String validateToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET))
                    .withIssuer("AgendaEdu")
                    .build()
                    .verify(token)
                    .getSubject(); // <- login que será usado para recuperar o usuário
        } catch (Exception e) {
            return null;
        }
    }
}

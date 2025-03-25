package com.agendaedu.educacional.Config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/error").permitAll()
            // Acesso público
            .requestMatchers(HttpMethod.POST, "/user").permitAll()
            .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/user/logout").authenticated()

            // Outros endpoints do usuário
            .requestMatchers("/user/**").authenticated()

            // Notificações (apenas autenticados)
            .requestMatchers("/notificacao/**").authenticated()

            // Sala: professores criam, alunos e professores acessam
            .requestMatchers(HttpMethod.POST, "/sala").hasRole("PROFESSOR")
            .requestMatchers(HttpMethod.POST, "/sala/atividades").hasRole("PROFESSOR")
            .requestMatchers(HttpMethod.GET, "/sala/atividades/sala/**").hasRole("PROFESSOR")

            .requestMatchers(HttpMethod.POST, "/sala/avisos").hasRole("PROFESSOR")
            .requestMatchers(HttpMethod.GET, "/sala/avisos").authenticated()
            .requestMatchers("/sala/**").authenticated()

            // Qualquer outra requisição também exige autenticação
            .anyRequest().authenticated()
        )
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    
}


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
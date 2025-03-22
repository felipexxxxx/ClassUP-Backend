package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Entities.Role;
import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Repositories.ClassRepository;
import com.agendaedu.educacional.Repositories.UserRepository;
import org.springframework.security.core.Authentication;
import java.util.UUID;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClassService {

    private final ClassRepository classRepository;
    private final UserRepository userRepository;

    @Transactional
    public ClassEntity createClass(ClassEntity classEntity) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) auth.getPrincipal();

    if (!user.getRole().equals(Role.PROFESSOR)) {
        throw new RuntimeException("Apenas professores podem criar salas.");
    }

    boolean salaExiste = classRepository.existsByNomeAndProfessor(classEntity.getNome(), user);
    if (salaExiste) {
        throw new RuntimeException("Você já criou uma sala com esse nome.");
    }

    String codigo = "SALA-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    classEntity.setProfessor(user);
    classEntity.setCodigoAcesso(codigo);
    return classRepository.save(classEntity);
}

    @Transactional
    public String joinClass(String codigoDeEntrada) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        ClassEntity classEntity = classRepository.findByCodigoAcesso(codigoDeEntrada)
                .orElseThrow(() -> new RuntimeException("Código de sala inválido"));

        if (user.getSala() != null && user.getSala().getId().equals(classEntity.getId())) {
        throw new RuntimeException("Você já está nessa sala.");
        }

        if (user.getSala() != null) {
        throw new RuntimeException("Você já está em uma sala. Só é permitido ingressar em uma.");
        }

        user.setSala(classEntity);
        userRepository.save(user);

        return "Usuário adicionado à sala com sucesso.";
    }
}

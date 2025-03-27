package com.agendaedu.educacional.Repositories.usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.agendaedu.educacional.Entities.sala.*;
import com.agendaedu.educacional.Entities.usuario.*;
import com.agendaedu.educacional.Enums.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMatricula(String matricula);
    Optional<User> findByCpf(String cpf); 
    List<User> findBySalaAndRole(ClassEntity sala, Role role);
    Optional<User> findByCodigoAutenticacao(String codigoAutenticacao);

}

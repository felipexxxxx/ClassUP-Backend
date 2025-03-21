package com.agendaedu.educacional.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.agendaedu.educacional.Entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMatricula(String matricula);
    Optional<User> findByCpf(String cpf); 
}

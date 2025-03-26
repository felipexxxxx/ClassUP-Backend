package com.agendaedu.educacional.Repositories.usuario;

import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Entities.usuario.UserSession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findTopByUserOrderByEntrouDesc(User user);
}

package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Entities.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<UserSession, Long> {
    void deleteBySaiuBefore(LocalDateTime time);
    Optional<UserSession> findTopByUserOrderByEntrouDesc(User user);
}

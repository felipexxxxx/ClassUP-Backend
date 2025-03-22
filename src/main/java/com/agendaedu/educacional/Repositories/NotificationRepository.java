// NotificationRepository.java
package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByAtividadeSalaId(Long salaId);
    List<Notification> findByUsuario_Id(Long userId);
}


package com.agendaedu.educacional.Services;

import com.agendaedu.educacional.Entities.Notification;
import com.agendaedu.educacional.Repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService; // Aqui usa o EmailService

    public void enviarNotificacao(String email, String assunto, String mensagem) {
        emailService.sendEmail(email, assunto, mensagem);
    }

    public List<Notification> buscarNotificacoesPorUsuario(Long userId) {
        return notificationRepository.findByUsuario_Id(userId);
    }
}

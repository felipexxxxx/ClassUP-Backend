package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.Entities.Notification;
import com.agendaedu.educacional.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacao")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Notification>> getByUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.buscarNotificacoesPorUsuario(userId));
    }

    @PostMapping("/enviar")
    public ResponseEntity<Void> enviarNotificacao(
            @RequestParam String email,
            @RequestParam String assunto,
            @RequestParam String mensagem
    ) {
        notificationService.enviarNotificacao(email, assunto, mensagem);
        return ResponseEntity.ok().build();
    }
}

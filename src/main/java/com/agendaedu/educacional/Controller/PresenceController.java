package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.Services.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/presencas")
@RequiredArgsConstructor
public class PresenceController {

    private final PresenceService absenceService;

    @PostMapping("/confirmar")
    public ResponseEntity<String> confirmarPresenca(
            @RequestParam Long userId,
            @RequestParam Long activityId
    ) {
        absenceService.confirmAttendance(userId, activityId);
        return ResponseEntity.ok("Presença confirmada com sucesso.");
    }

    @DeleteMapping("/cancelar")
    public ResponseEntity<String> cancelarPresenca(
            @RequestParam Long userId,
            @RequestParam Long activityId
    ) {
        absenceService.cancelAttendance(userId, activityId);
        return ResponseEntity.ok("Presença cancelada com sucesso.");
    }
}

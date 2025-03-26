package com.agendaedu.educacional.Controller.sala;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.agendaedu.educacional.DTOs.atividade.StudentActivityDTO;
import com.agendaedu.educacional.DTOs.sala.ClassDTO;
import com.agendaedu.educacional.DTOs.sala.JoinClassDTO;
import com.agendaedu.educacional.Services.atividade.ActivityService;
import com.agendaedu.educacional.Services.presenca.PresenceService;
import com.agendaedu.educacional.Services.sala.ClassService;

import java.util.List;

@RestController
@RequestMapping("/aluno/sala")
@RequiredArgsConstructor
public class StudentClassController {

    private final ClassService classService;
    private final ActivityService activityService;
    private final PresenceService presenceService;

    @PostMapping("/entrar")
    public ResponseEntity<String> joinClass(@RequestBody JoinClassDTO dto) {
        return ResponseEntity.ok(classService.joinClass(dto.codigoAcesso()));
    }

    @GetMapping
    public ResponseEntity<ClassDTO> getMinhaSala() {
        return ResponseEntity.ok(classService.getMinhaSalaAtual());
    }

    @GetMapping("/atividades")
    public ResponseEntity<List<StudentActivityDTO>> listarMinhasAtividades() {
        return ResponseEntity.ok(activityService.listarAtividadesDoAluno());
    }

    @PutMapping("/atividades/{id}/confirmar")
    public ResponseEntity<String> confirmarPresenca(@PathVariable Long id) {
        presenceService.confirmarPresenca(id);
        return ResponseEntity.ok("Presença confirmada com sucesso.");
    }

    @PutMapping("/atividades/{id}/cancelar")
    public ResponseEntity<String> cancelarPresenca(@PathVariable Long id) {
        presenceService.cancelarPresenca(id);
        return ResponseEntity.ok("Presença cancelada com sucesso.");
    }
}

package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.DTOs.ActivityDTO;
import com.agendaedu.educacional.DTOs.JoinClassDTO;
import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Entities.ClassHistoryEntity;
import com.agendaedu.educacional.Services.ClassService;
import com.agendaedu.educacional.Services.ActivityService;
import com.agendaedu.educacional.Services.PresenceService;
import com.agendaedu.educacional.DTOs.StudentActivityDTO;
import com.agendaedu.educacional.Entities.Activity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sala")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final ActivityService activityService;
    private final PresenceService presenceService;

    @PostMapping
    public ResponseEntity<ClassEntity> createClass(@RequestBody ClassEntity classEntity) {
        return ResponseEntity.ok(classService.createClass(classEntity));
    }

    @PostMapping("/entrar")
    public ResponseEntity<String> joinClass(@RequestBody JoinClassDTO dto) {
        return ResponseEntity.ok(classService.joinClass(dto.codigoAcesso()));
    }

    @PostMapping("/encerrar")
    public ResponseEntity<String> encerrarSemestre() {
        return ResponseEntity.ok(classService.encerrarSemestre());
    }

    @GetMapping("/historico")
    public ResponseEntity<List<ClassHistoryEntity>> getHistoricoUsuario() {
        return ResponseEntity.ok(classService.listarHistoricoUsuario());
    }

    @DeleteMapping("/aluno/{alunoId}")
    public ResponseEntity<String> removerAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(classService.removerAlunoDaSala(alunoId));
    }

    //  Atividades
    //  Criar atividade (PROFESSOR)
    @PostMapping("/atividades")
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        return ResponseEntity.ok(activityService.createActivity(activity));
    }
    // Listar atividades do aluno
    @GetMapping("/atividades")
    public ResponseEntity<List<StudentActivityDTO>> listarMinhasAtividades() {
        return ResponseEntity.ok(activityService.listarAtividadesDoAluno());
    }
    // Listar atividades da sala(PROFESSOR)
    @GetMapping("/atividades/sala/{salaId}")
    public ResponseEntity<List<ActivityDTO>> getActivitiesBySala(@PathVariable Long salaId) {
        return ResponseEntity.ok(activityService.getActivitiesBySala(salaId));
    }

    // Atividade por ID
    @GetMapping("/atividades/{id}")
    public ResponseEntity<Activity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getById(id));
    }

    // 🔽 Presença
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




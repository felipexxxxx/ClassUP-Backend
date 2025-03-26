package com.agendaedu.educacional.Controller.sala;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.agendaedu.educacional.DTOs.atividade.*;
import com.agendaedu.educacional.DTOs.aviso.*;
import com.agendaedu.educacional.DTOs.sala.*;
import com.agendaedu.educacional.Entities.atividade.*;
import com.agendaedu.educacional.Entities.aviso.*;
import com.agendaedu.educacional.Entities.sala.*;
import com.agendaedu.educacional.Services.atividade.*;
import com.agendaedu.educacional.Services.aviso.*;
import com.agendaedu.educacional.Services.sala.*;

import java.util.List;

@RestController
@RequestMapping("/professor/sala")
@RequiredArgsConstructor
public class TeacherClassController {

    private final ClassService classService;
    private final ActivityService activityService;
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<ClassDTO>> getSalasDoProfessor() {
        return ResponseEntity.ok(classService.getSalasDoProfessor());
    }

    @PostMapping
    public ResponseEntity<ClassEntity> createClass(@RequestBody ClassEntity classEntity) {
        return ResponseEntity.ok(classService.createClass(classEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetClassDetalhadoProfessorDTO> getDetalhesPorId(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getDetalhesSalaPorId(id));
    }

    @PostMapping("/encerrar")
    public ResponseEntity<String> encerrarSemestre() {
        return ResponseEntity.ok(classService.encerrarSemestre());
    }

    @PostMapping("/atividades")
    public ResponseEntity<Activity> criarAtividade(@RequestBody Activity activity) {
        return ResponseEntity.ok(activityService.criarAtividade(activity));
    }

    @PutMapping("/atividades/{id}")
    public ResponseEntity<String> editarAtividade(@PathVariable Long id, @RequestBody ActivityDTO dto) {
        activityService.editarAtividade(id, dto);
        return ResponseEntity.ok("Atividade atualizada com sucesso.");
    }

    @DeleteMapping("/atividades/{id}")
    public ResponseEntity<Void> deletarAtividade(@PathVariable Long id) {
        activityService.deletarAtividade(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/avisos")
    public ResponseEntity<String> createNotice(@RequestBody NoticeDTO dto) {
        return ResponseEntity.ok(noticeService.createNotice(dto));
    }

    @PutMapping("/avisos/{id}")
    public ResponseEntity<Void> editarAviso(@PathVariable Long id, @RequestBody Notice avisoAtualizado) {
        noticeService.editarAviso(id, avisoAtualizado);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/avisos/{id}")
    public ResponseEntity<Void> excluirAviso(@PathVariable Long id) {
        noticeService.excluirAviso(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/aluno/{alunoId}")
    public ResponseEntity<String> removerAluno(@PathVariable Long alunoId) {
        return ResponseEntity.ok(classService.removerAlunoDaSala(alunoId));
    }

    @GetMapping("/atividades/{atividadeId}/resumo")
    public ResponseEntity<ActivityResumoDTO> getResumoAtividade(@PathVariable Long atividadeId) {
        return ResponseEntity.ok(activityService.getResumoAtividade(atividadeId));
    }
}

    
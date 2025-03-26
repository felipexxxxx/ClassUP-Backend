package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.DTOs.ActivityDTO;
import com.agendaedu.educacional.DTOs.ActivityResumoDTO;
import com.agendaedu.educacional.DTOs.ClassHistoryDetalhesDTO;
import com.agendaedu.educacional.DTOs.GetClassDTO;
import com.agendaedu.educacional.DTOs.GetClassDetalhadoDTO;
import com.agendaedu.educacional.DTOs.JoinClassDTO;
import com.agendaedu.educacional.DTOs.NoticeDTO;
import com.agendaedu.educacional.DTOs.ProfessorSalaDTO;
import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Entities.ClassHistoryEntity;
import com.agendaedu.educacional.Entities.Notice;
import com.agendaedu.educacional.Services.ClassService;
import com.agendaedu.educacional.Services.NoticeService;
import com.agendaedu.educacional.Services.ActivityService;
import com.agendaedu.educacional.Services.PresenceService;
import com.agendaedu.educacional.DTOs.StudentActivityDTO;
import com.agendaedu.educacional.DTOs.StudentClassDTO;
import com.agendaedu.educacional.DTOs.ActivityResumoDTO;
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
    private final NoticeService noticeService;

    @PostMapping
    public ResponseEntity<ClassEntity> createClass(@RequestBody ClassEntity classEntity) {
        return ResponseEntity.ok(classService.createClass(classEntity));
    }

    @GetMapping
    public ResponseEntity<List<ProfessorSalaDTO>> getSalasDoProfessor() {
        return ResponseEntity.ok(classService.getSalasDoProfessor());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GetClassDetalhadoDTO> getDetalhesPorId(@PathVariable Long id) {
        return ResponseEntity.ok(classService.getDetalhesSalaPorId(id));
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

    @GetMapping("/historico/{id}")
        public ResponseEntity<ClassHistoryDetalhesDTO> verDetalhesHistorico(@PathVariable Long id) {
    return ResponseEntity.ok(classService.buscarDetalhesHistorico(id));
    }



    @GetMapping("/aluno")
    public ResponseEntity<StudentClassDTO> getMinhaSala() {
        return ResponseEntity.ok(classService.getMinhaSalaAtual());
    }

    @GetMapping("/detalhes")
    public ResponseEntity<GetClassDTO> getDetalhesSala() {
        return ResponseEntity.ok(classService.detalharSalaDoAluno());
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

    @GetMapping("/atividades/{atividadeId}/resumo")
    public ResponseEntity<ActivityResumoDTO> getResumoAtividade(@PathVariable Long atividadeId) {
        return ResponseEntity.ok(activityService.getResumoAtividade(atividadeId));
    }

    



    @DeleteMapping("/atividades/{id}")
    public ResponseEntity<Void> deletarAtividade(@PathVariable Long id) {
        activityService.deletarAtividade(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atividades/{id}")
    public ResponseEntity<String> editarAtividade(@PathVariable Long id, @RequestBody ActivityDTO dto) {
        activityService.editarAtividade(id, dto);
        return ResponseEntity.ok("Atividade atualizada com sucesso.");
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

     @PostMapping("/avisos")
    public ResponseEntity<String> createNotice(@RequestBody NoticeDTO dto) {
        return ResponseEntity.ok(noticeService.createNotice(dto));
    }

    @GetMapping("/avisos")
    public ResponseEntity<List<Notice>> getNotices() {
        return ResponseEntity.ok(noticeService.getNoticesByUserClass());
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

}




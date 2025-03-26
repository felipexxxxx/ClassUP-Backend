package com.agendaedu.educacional.Controller.sala;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.agendaedu.educacional.DTOs.sala.GetClassDetalhadoAlunoDTO;
import com.agendaedu.educacional.DTOs.sala.JoinClassDTO;
import com.agendaedu.educacional.Services.presenca.PresenceService;
import com.agendaedu.educacional.Services.sala.ClassService;

@RestController
@RequestMapping("/aluno/sala")
@RequiredArgsConstructor
public class StudentClassController {

    private final ClassService classService;

    private final PresenceService presenceService;

    @PostMapping("/entrar")
    public ResponseEntity<String> joinClass(@RequestBody JoinClassDTO dto) {
        return ResponseEntity.ok(classService.joinClass(dto.codigoAcesso()));
    }

    @GetMapping("/detalhes")
    public ResponseEntity<GetClassDetalhadoAlunoDTO> getDetalhesSala() {
        return ResponseEntity.ok(classService.detalharSalaDoAluno());
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

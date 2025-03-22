package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.DTOs.JoinClassDTO;
import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Entities.ClassHistoryEntity;
import com.agendaedu.educacional.Services.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/class")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<ClassEntity> createClass(@RequestBody ClassEntity classEntity) {
        ClassEntity created = classService.createClass(classEntity);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/entrar")
    public ResponseEntity<String> joinClass(@RequestBody JoinClassDTO dto) {
        String result = classService.joinClass(dto.codigoAcesso());
        return ResponseEntity.ok(result);
}

    @PostMapping("/encerrar")
    public ResponseEntity<String> encerrarSemestre() {
        String mensagem = classService.encerrarSemestre();
        return ResponseEntity.ok(mensagem);
}
    @GetMapping("/historico")
    public ResponseEntity<List<ClassHistoryEntity>> getHistoricoUsuario() {
        List<ClassHistoryEntity> historico = classService.listarHistoricoUsuario();
        return ResponseEntity.ok(historico);
}
    @DeleteMapping("/aluno/{alunoId}")
    public ResponseEntity<String> removerAluno(@PathVariable Long alunoId) {
        String msg = classService.removerAlunoDaSala(alunoId);
        return ResponseEntity.ok(msg);
}


}

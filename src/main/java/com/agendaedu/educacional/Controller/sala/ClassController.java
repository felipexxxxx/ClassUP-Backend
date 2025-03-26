package com.agendaedu.educacional.Controller.sala;

import com.agendaedu.educacional.DTOs.sala.*;
import com.agendaedu.educacional.Entities.sala.*;
import com.agendaedu.educacional.Services.sala.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sala")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @GetMapping("/historico")
    public ResponseEntity<List<ClassHistoryEntity>> getHistoricoUsuario() {
        return ResponseEntity.ok(classService.listarHistoricoUsuario());
    }

    @GetMapping("/historico/{id}")
    public ResponseEntity<ClassHistoryDetalhesDTO> verDetalhesHistorico(@PathVariable Long id) {
        return ResponseEntity.ok(classService.verSalaHistorico(id));
    }

    
}

    






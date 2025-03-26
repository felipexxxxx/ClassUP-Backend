package com.agendaedu.educacional.Controller.sala;


import com.agendaedu.educacional.DTOs.sala.ClassHistoryDetalhesDTO;
import com.agendaedu.educacional.DTOs.sala.GetClassDTO;
import com.agendaedu.educacional.Entities.aviso.Notice;
import com.agendaedu.educacional.Entities.sala.ClassHistoryEntity;
import com.agendaedu.educacional.Services.aviso.NoticeService;
import com.agendaedu.educacional.Services.sala.ClassService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sala")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;
    private final NoticeService noticeService;

    @GetMapping("/detalhes")
    public ResponseEntity<GetClassDTO> getDetalhesSala() {
        return ResponseEntity.ok(classService.detalharSalaDoAluno());
    }

    @GetMapping("/historico")
    public ResponseEntity<List<ClassHistoryEntity>> getHistoricoUsuario() {
        return ResponseEntity.ok(classService.listarHistoricoUsuario());
    }

    @GetMapping("/historico/{id}")
    public ResponseEntity<ClassHistoryDetalhesDTO> verDetalhesHistorico(@PathVariable Long id) {
        return ResponseEntity.ok(classService.buscarDetalhesHistorico(id));
    }

    @GetMapping("/avisos")
    public ResponseEntity<List<Notice>> getNotices() {
        return ResponseEntity.ok(noticeService.getNoticesByUserClass());
    }

    
}

    






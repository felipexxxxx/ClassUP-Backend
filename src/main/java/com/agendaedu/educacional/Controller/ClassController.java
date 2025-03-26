package com.agendaedu.educacional.Controller;


import com.agendaedu.educacional.DTOs.ClassHistoryDetalhesDTO;
import com.agendaedu.educacional.DTOs.GetClassDTO;
import com.agendaedu.educacional.Entities.ClassHistoryEntity;
import com.agendaedu.educacional.Entities.Notice;
import com.agendaedu.educacional.Services.ClassService;
import com.agendaedu.educacional.Services.NoticeService;



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

    






package com.agendaedu.educacional.DTOs.sala;

import com.agendaedu.educacional.DTOs.atividade.StudentActivityDTO;
import com.agendaedu.educacional.DTOs.usuario.SimpleUserDTO;
import com.agendaedu.educacional.Entities.aviso.Notice;

import java.util.List;

public record GetClassDetalhadoAlunoDTO(
    Long id,
    String nome,
    String codigoAcesso,
    SimpleUserDTO professor,
    List<SimpleUserDTO> alunos,
    List<StudentActivityDTO> atividades,
    List<Notice> avisos
) {}

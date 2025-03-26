package com.agendaedu.educacional.DTOs.sala;

import com.agendaedu.educacional.DTOs.atividade.*;
import com.agendaedu.educacional.DTOs.usuario.*;
import com.agendaedu.educacional.Entities.aviso.*;

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

package com.agendaedu.educacional.DTOs.sala;

import com.agendaedu.educacional.DTOs.atividade.ActivityDTO;
import com.agendaedu.educacional.DTOs.usuario.SimpleUserDTO;
import com.agendaedu.educacional.Entities.aviso.Notice;

import java.util.List;

public record GetClassDetalhadoProfessorDTO(
    Long id,
    String nome,
    String codigoAcesso,
    SimpleUserDTO professor,
    List<SimpleUserDTO> alunos,
    List<ActivityDTO> atividades,
    List<Notice> avisos
) {}
